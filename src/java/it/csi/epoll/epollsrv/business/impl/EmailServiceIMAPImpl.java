/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import it.csi.epoll.epollsrv.business.dto.ConfigDTO;
import it.csi.epoll.epollsrv.business.dto.email.AllegatoDTO;
import it.csi.epoll.epollsrv.business.dto.email.EmailConfigDTO;
import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.dto.email.MimeType;
import it.csi.epoll.epollsrv.business.service.EmailServiceIMAP;
import it.csi.epoll.epollsrv.business.service.PecCfgService;
import it.csi.epoll.epollsrv.integration.domain.PecCfg;
import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.ErrorMessages;
import it.csi.epoll.epollsrv.util.email.ricerca.AbsSearchTerm;
import it.csi.epoll.epollsrv.util.exception.ManagedException;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 *
 */
@Service
@Transactional ( propagation = Propagation.NOT_SUPPORTED )
public class EmailServiceIMAPImpl extends AbsEmailServices implements EmailServiceIMAP {

    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_BUSINESS_LOG_CATEGORY, "EmailServiceIMAPImpl" );

    private Session mailSession;

    @Autowired
    private PecCfgService pecCfgService;

    @Autowired
    private ConfigDTO configDTO;

    private static boolean connected = false;

    /**
     * Usato per sincronizzare la connessione alla email e settaggio della property connected.
     */
    private final Object connectionLock = new Object ();

    @Override
    public List<EmailDTO> searchMail ( AbsSearchTerm search ) {
        final String name = "searchMail";
        List<EmailDTO> retList = new LinkedList<> ();
        Assert.isTrue ( connected, ErrorMessages.E_NON_CONNESSO );
        Assert.notNull ( search.getIdEnte (), ErrorMessages.G_PARAMETRO_ENTE_OBBLIGATORIO_NON_SPECIFICATO );
        try {
            Folder inbox = getPecConfigCached ( search.getIdEnte () ).getInbox ();
            Assert.isTrue ( inbox.exists (), String.format ( ErrorMessages.E_INBOX_INESISTENTE, inbox.getName () ) );
            Assert.isTrue ( inbox.isOpen (), String.format ( ErrorMessages.E_INBOX_NON_APERTA, inbox.getName () ) );
            retList = parseMessaggesToDTO ( inbox.search ( search ), search.getIdEnte () );
            LOGGER.info ( name, String.format ( Constants.E_INFO_EMAIL, retList.size () ) );
        } catch ( MessageRemovedException mre ) {
            LOGGER.warn ( name, ErrorMessages.E_INBOX_ERRORE_RICERCA, mre );
        } catch ( MessagingException | IOException e ) {
            LOGGER.error ( name, ErrorMessages.E_INBOX_ERRORE_RICERCA, e );
        }
        return retList;
    }

    @Override
    public boolean deleteMail ( Message msg ) {
        if ( null != msg ) {
            try {
                msg.setFlag ( Flag.DELETED, Boolean.TRUE );
                // valutare se chiamare: msg.saveChanges ()
            } catch ( MessagingException e ) {
                try {
                    LOGGER.error ( "deleteMail", String.format ( ErrorMessages.E_ERRORE_CANCELLAZIONE, msg.getMessageNumber (), msg.getSubject () ), e );
                } catch ( MessagingException e1 ) {
                    LOGGER.error ( "deleteMail", ErrorMessages.G_ERRORE_GENERICO, e );
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void connectMail () {
        synchronized ( connectionLock ) {
            if ( !connected ) {
                Properties props = System.getProperties ();
                props.setProperty ( "mail.store.protocol", ConfigDTO.PROTOCOL_IMAP );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".auth", "true" );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".ssl.enable", "true" ); // attiva ssl e imposta la porta standard 993
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".starttls.enable", "true" );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".ssl.protocols", "TLSv1.2" );

                mailSession = Session.getDefaultInstance ( props, null );
                mailSession.setDebug ( Boolean.parseBoolean ( configDTO.getDebug () ) ); // purtroppo non si può comandare dalla tabella.
                connected = Boolean.TRUE;
            }
        }
    }

    @Override
    public void disconnectMail ( Long idEnte ) {
        Assert.notNull ( idEnte, ErrorMessages.G_PARAMETRO_ENTE_OBBLIGATORIO_NON_SPECIFICATO );
        EmailConfigDTO config = cachedConfiguration.get ( idEnte );
        if ( null != config ) {
            if ( config.isConnectedIMAP () ) {
                try {
                    if ( ( null != config.getInbox () ) && config.getInbox ().isOpen () ) {
                        //in fase di chiusura cancello tutti i messaggi con flag = deleted
                        config.getInbox ().close ( Boolean.TRUE );
                    }
                    if ( ( null != config.getStore () ) && config.getStore ().isConnected () ) {
                        config.getStore ().close ();
                    }
                } catch ( MessagingException e ) {
                    LOGGER.error ( "disconnectMail", ErrorMessages.E_ERRORE_DISCONESSIONE, e );
                    throw new ManagedException ( ErrorMessages.E_ERRORE_DISCONESSIONE, e );
                }
            }
            cachedConfiguration.remove ( idEnte );
        }
    }

    @Override
    public boolean isConnected () {
        return connected;
    }

    /**
     * Trasforma una lista di Message [] in una lista di DTO per le manipolazioni
     * 
     * @param messages
     * @param idEnte
     * @return List<EmailDTO>
     * @throws MessagingException
     * @throws IOException
     */
    private List<EmailDTO> parseMessaggesToDTO ( Message [] messages, Long idEnte ) throws MessagingException, IOException {
        List<EmailDTO> retList = new LinkedList<> ();
        if ( ( null != messages ) && ( messages.length > 0 ) ) {
            for ( Message message: messages ) {
                retList.add ( parseEmailToDTO ( message, idEnte ) );
            }
        }
        return retList;
    }

    @Override
    public EmailDTO parseEmailToDTO ( Message message, Long idEnte ) throws MessagingException, IOException {
        try {
            StringBuilder bodyText = new StringBuilder ();
            StringBuilder bodyHtml = new StringBuilder (); //Da usare eventualmente
            EmailDTO email = new EmailDTO ( idEnte );
            email.setEmailColl ( message );
            ArrayList<AllegatoDTO> allegatiList = new ArrayList<> ();

            Message msg = message;
            boolean ricevutaPecConsegna = false;
            boolean foundEncapsulateMessageRFC822 = false;

            String [] xRicevuta = message.getHeader ( Constants.E_HEADER_X_RICEVUTA );

            if ( ( xRicevuta != null ) && ( xRicevuta.length > 0 ) ) {
                email.setTipoMail ( xRicevuta [0] );
                if ( Constants.E_TIPO_MAIL_RICEVUTA_CONSEGNA.equals ( xRicevuta [0] ) ) {
                    ricevutaPecConsegna = true;
                }
            }
            StringBuilder oggettoEmail = new StringBuilder ();
            dumpPart ( msg, allegatiList, bodyText, bodyHtml, oggettoEmail, ricevutaPecConsegna, foundEncapsulateMessageRFC822 );
            //TO
            email.setTo ( EmailServiceIMAP.extractRecipientType ( msg, Message.RecipientType.TO ) );
            //CC
            email.setCc ( EmailServiceIMAP.extractRecipientType ( msg, Message.RecipientType.CC ) );

            email.setFrom ( EmailServiceIMAP.extractEmail ( msg.getFrom () [0].toString () ) );

            email.setHeaders ( extractHeaders ( msg ) );

            email.setAllegati ( allegatiList );

            if ( StringUtils.hasText ( bodyText ) ) {
                email.setBody ( bodyText.toString () );
                email.setMimeType ( MimeType.TEXT_PLAIN );
            }
            if ( StringUtils.hasText ( bodyHtml ) ) {
                email.setBody ( bodyHtml.toString () );
                email.setMimeType ( MimeType.TEXT_HTML );
            }
            email.setSentDate ( msg.getSentDate () );
            if(msg.getReceivedDate() != null) {
            	email.setReceivedDate ( msg.getReceivedDate () );
            }else {
            	email.setReceivedDate ( new Date() );
            }
            
            if ( StringUtils.hasText ( oggettoEmail ) ) {
                email.setOggetto ( oggettoEmail.toString () );
            } else {
                email.setOggetto ( msg.getSubject () );
            }

            return email;
        } catch ( MessagingException me ) {
            LOGGER.error ( "parseEmailToDTO", ErrorMessages.E_ERRORE_PARSIFICAZIONE, me );
            throw new ManagedException ( ErrorMessages.E_ERRORE_PARSIFICAZIONE, me );
        }
    }

    /**
     * Ho provato a ridurre la complessita', chissa' se funziona ancora
     * 
     * @param part
     * @param allegatiList
     * @param bodyText
     * @param bodyHtml
     * @param ricevutaPecConsegna
     * @param foundEncapsulateMessageRFC822
     * @return intero senza significato.
     * @throws MessagingException
     * @throws IOException
     */
    @SuppressWarnings ( "all" )
    private int dumpPart ( Part part, ArrayList<AllegatoDTO> allegatiList, StringBuilder bodyText, StringBuilder bodyHtml,
        StringBuilder oggettoEmail,
        boolean ricevutaPecConsegna,
        boolean foundEncapsulateMessageRFC822 ) throws MessagingException, IOException {

        if ( part.isMimeType ( Constants.MIME_TYPE_MULTIPART_GENERIC ) ) {
            Multipart mp = (Multipart) part.getContent ();
            for ( int m = 0; m < mp.getCount (); m++ ) {
                dumpPart ( mp.getBodyPart ( m ), allegatiList, bodyText, bodyHtml, oggettoEmail, ricevutaPecConsegna, foundEncapsulateMessageRFC822 );
            }
        } else if ( part.getFileName () != null ) {

            if ( part.isMimeType ( Constants.MIME_TYPE_MESSAGE_RFC822 ) && !foundEncapsulateMessageRFC822 ) {
                foundEncapsulateMessageRFC822 = true;
                if ( !ricevutaPecConsegna ) {
                    try {
                        oggettoEmail.append ( ( (Message) part.getContent () ).getSubject () );
                    } catch ( Exception e ) {
                        LOGGER.warn ( "dumpPart", ErrorMessages.E_ERRORE_PARSIFICAZIONE_EMAIL );
                    }
                    bodyText.setLength ( 0 );
                    bodyHtml.setLength ( 0 );
                    dumpPart ( (Part) part.getContent (), allegatiList, bodyText, bodyHtml, oggettoEmail, ricevutaPecConsegna, foundEncapsulateMessageRFC822 );
                }
            } else {
                AllegatoDTO attach = new AllegatoDTO ();
                attach.setNomeAllegato ( mimeDecodeFileName ( part.getFileName () ) );
                String tipoM = part.getContentType ();

                if ( tipoM.indexOf ( ';' ) > 0 ) {
                    attach.setTipoMIME ( tipoM.substring ( 0, tipoM.indexOf ( ';' ) ) );
                } else {
                    attach.setTipoMIME ( tipoM );
                }

                //                attach.setAllegato ( IOUtils.toByteArray ( part.getInputStream () ) );
                allegatiList.add ( attach );
            }
        } else if ( part.getFileName () == null ) {
            if ( part.isMimeType ( MimeType.TEXT_HTML.getValue () ) ) {
                if ( bodyHtml.length () == 0 ) {
                    bodyHtml.append ( ( (String) part.getContent () ) );
                }
            } else if ( part.isMimeType ( MimeType.TEXT_PLAIN.getValue () ) ) {
                if ( bodyText.length () == 0 ) {
                    bodyText.append ( ( (String) part.getContent () ) );
                }
            } else if ( part.isMimeType ( Constants.MIME_TYPE_MESSAGE_RFC822 ) && !foundEncapsulateMessageRFC822 ) {
                foundEncapsulateMessageRFC822 = true;
                bodyText.setLength ( 0 );
                bodyHtml.setLength ( 0 );
                dumpPart ( (Part) part.getContent (), allegatiList, bodyText, bodyHtml, oggettoEmail, ricevutaPecConsegna, foundEncapsulateMessageRFC822 );
            } else if ( part.isMimeType ( Constants.MIME_TYPE_MESSAGE_RFC822 ) && foundEncapsulateMessageRFC822 ) {
                AllegatoDTO attach = new AllegatoDTO ();
                attach.setNomeAllegato ( Constants.NO_FILE_NAME_MESSAGE_RFC822 );
                attach.setTipoMIME ( Constants.MIME_TYPE_MESSAGE_RFC822 );
                //                attach.setAllegato ( IOUtils.toByteArray ( part.getInputStream () ) );
                allegatiList.add ( attach );
            }
        }
        return 0;
    }

    private static String mimeDecodeFileName ( String s ) {
        String decodedString = null;
        try {
            decodedString = MimeUtility.decodeText ( s );
        } catch ( UnsupportedEncodingException e ) {
            LOGGER.warn ( "mimeDecodeFileName", ErrorMessages.E_ERRORE_NOME_ALLEGATO, e );
            String charsetUnKnow = e.getMessage ();
            if ( charsetUnKnow.equalsIgnoreCase ( Constants.CHARSET_UNKNOWN ) ) {
                try {
                    decodedString = MimeUtility.decodeText ( s.replaceAll ( "(?i)" + charsetUnKnow, Constants.CHARSET_ISO_8859 ) );
                    return decodedString;
                } catch ( UnsupportedEncodingException ignoreUns2 ) {
                    return s;
                }
            }
            return s;
        }
        return decodedString;
    }

    /**
     * Metodo che permette di estrarre tutti gli header dalla mail
     * 
     * @param message messaggio da cui estrarre gli headers
     * @return mappa contenente tutti gli headers.
     */
    private Map<String, String> extractHeaders ( Message message ) {
        Map<String, String> ret = new HashMap<> ();
        try {
            for ( @SuppressWarnings ( "unchecked" )
            Enumeration<Header> headersEnum = message.getAllHeaders (); headersEnum.hasMoreElements (); ) {
                Header header = headersEnum.nextElement ();
                ret.put ( header.getName (), header.getValue () );
            }
        } catch ( MessagingException e ) {
            LOGGER.error ( "extractHeaders", ErrorMessages.E_ERRORE_PARSIFICAZIONE, e );
        }
        return ret;
    }

    @Override
    protected EmailConfigDTO getConfig ( Long idEnte ) {
        PecCfg pecCfg = null;
        try {
            pecCfg = pecCfgService.findOneByEnteId ( idEnte );
        } catch ( Exception e ) {
            LOGGER.error ( "getConfig", String.format ( ErrorMessages.E_ERRORE_CONFIG_RECUPERO, idEnte ), e );
            throw new ManagedException ( String.format ( ErrorMessages.E_ERRORE_CONFIG_RECUPERO, idEnte ), e );
        }
        Assert.notNull ( pecCfg, String.format ( ErrorMessages.E_ERRORE_CONFIG_NON_TROVATA, idEnte ) );
        if ( !connected ) {
            connectMail ();
        }
        try {
            Store store = mailSession.getStore ( ConfigDTO.PROTOCOL_IMAP );
            store.connect ( pecCfg.getImapServerHostname (), pecCfg.getImapUsername (), new String ( pecCfg.getImapPassword (), StandardCharsets.UTF_8 ) );

            Folder inbox = store.getFolder ( pecCfg.getImapInboxFolder () );
            inbox.open ( Folder.READ_WRITE );

            return EmailConfigDTO.builder ()
                .withPassword ( new String ( pecCfg.getImapPassword (), StandardCharsets.UTF_8 ) )
                .withServerHostnameIMAP ( pecCfg.getImapServerHostname () )
                .withServerHostnameSMTP ( pecCfg.getSmtpServerHostname () )
                .withUsername ( pecCfg.getImapUsername () )
                .withInboxFolder ( pecCfg.getImapInboxFolder () )
                .withIndirizzoPec ( pecCfg.getIndirizzoPec () )
                .withStore ( store )
                .withInbox ( inbox )
                .withConnectedIMAP ( true )
                .build ();

        } catch ( MessagingException e ) {
            LOGGER.error ( "getConfig", ErrorMessages.E_ERRORE_CONNESSIONE, e );
            throw new ManagedException ( ErrorMessages.E_ERRORE_CONNESSIONE, e );
        }
    }
}
