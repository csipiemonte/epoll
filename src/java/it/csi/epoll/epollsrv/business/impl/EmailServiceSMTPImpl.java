/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.function.BiConsumer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import it.csi.epoll.epollsrv.business.dto.ConfigDTO;
import it.csi.epoll.epollsrv.business.dto.email.AllegatoDTO;
import it.csi.epoll.epollsrv.business.dto.email.EPollMimeMessage;
import it.csi.epoll.epollsrv.business.dto.email.EmailConfigDTO;
import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.dto.email.MimeType;
import it.csi.epoll.epollsrv.business.service.EmailServiceSMTP;
import it.csi.epoll.epollsrv.business.service.PecCfgService;
import it.csi.epoll.epollsrv.integration.domain.PecCfg;
import it.csi.epoll.epollsrv.util.ErrorMessages;
import it.csi.epoll.epollsrv.util.exception.ManagedException;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 * 
 */
@Service
@Transactional ( propagation = Propagation.NOT_SUPPORTED )
public class EmailServiceSMTPImpl extends AbsEmailServices implements EmailServiceSMTP {

    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_BUSINESS_LOG_CATEGORY, "EMailServiceSMTPImpl" );

    @Autowired
    private PecCfgService pecCfgService;
    
    @Autowired
    private ConfigDTO configDTO;

    private Session mailSession;

    private static boolean connected = false;

    /**
     * Usato per sincronizzare la connessione alla email e settaggio della property connected.
     */
    private final Object connectionLock = new Object ();

    @Override
    public Boolean sendMail ( EmailDTO datiMail ) {
        Assert.notNull ( datiMail, String.format ( ErrorMessages.G_PARAMETRO_OBBLIGATORIO_NON_SPECIFICATO, "datiMail" ) );
        Assert.notNull ( datiMail.getIdEnte (), ErrorMessages.G_PARAMETRO_ENTE_OBBLIGATORIO_NON_SPECIFICATO );
        Assert.notEmpty ( datiMail.getTo (), String.format ( ErrorMessages.G_PARAMETRO_OBBLIGATORIO_NON_SPECIFICATO, "To" ) );
        Assert.hasText ( datiMail.getBody (), String.format ( ErrorMessages.G_PARAMETRO_OBBLIGATORIO_NON_SPECIFICATO, "Body" ) );
        Assert.hasText ( datiMail.getOggetto (), String.format ( ErrorMessages.G_PARAMETRO_OBBLIGATORIO_NON_SPECIFICATO, "Oggetto" ) );
        Assert.notEmpty ( datiMail.getHeaders (), String.format ( ErrorMessages.G_PARAMETRO_OBBLIGATORIO_NON_SPECIFICATO, "Headers" ) );
        Transport transport = null;
        try {
            // Creo l'oggetto della mail prima della connessione per tenerla aperta il meno possibile. Usare i thread per migliorare le performance.
            EPollMimeMessage emailToSend = buildMessage ( datiMail );
            if ( !connected ) {
                connectMail ();
            }
            transport = mailSession.getTransport ( ConfigDTO.PROTOCOL_SMTP );
            EmailConfigDTO config = getPecConfigCached ( datiMail.getIdEnte () );
            transport.connect ( config.getServerHostnameSMTP (), config.getUsername (), config.getPassword () );
            transport.sendMessage ( emailToSend, listToAdress ( datiMail.getTo () ) );

            return Boolean.TRUE;
        } catch ( Exception e ) {
            LOGGER.error ( "sendMail", ErrorMessages.E_ERRORE_INVIO_MAIL, e );
            return Boolean.FALSE;
        } finally {
            try {
                if ( null != transport && transport.isConnected()) {
                    transport.close ();
                }
            } catch ( MessagingException e ) {
                LOGGER.warn ( "sendMail", ErrorMessages.E_ERRORE_INVIO_MAIL, e );
            }
        }

    }

    /**
     * Metodo per la connessione al server smtp. Da eseguire allo startup del server
     */
    @Override
    public void connectMail () {
        synchronized ( connectionLock ) {
            if ( !connected ) {
                Properties props = System.getProperties ();
                props.setProperty ( "mail.store.protocol", ConfigDTO.PROTOCOL_SMTP );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_SMTP + ".auth", "true" );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_SMTP + ".ssl.enable", "true" ); // attiva ssl e imposta la porta standard 993
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_SMTP + ".starttls.enable", "true" );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_SMTP + ".ssl.protocols", "TLSv1.2" );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_SMTP + ".connectiontimeout", "" + configDTO.getConnectionTimeout () );
                props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_SMTP + ".timeout", "" + configDTO.getEmailTimeout () );
                mailSession = Session.getDefaultInstance ( props, null );
                mailSession.setDebug ( Boolean.parseBoolean ( configDTO.getDebug () ) ); // purtroppo non si può comandare dalla tabella.
                connected = Boolean.TRUE;
            }
        }
    }

    /**
     * Metodo per la creazione dell'oggetto che rappresenta la mail da inviare
     * 
     * @param datiMail DTO
     * @return EPollMimeMessage oggetto email
     * @throws MessagingException Errore in fase di creazione della mail
     */
    private EPollMimeMessage buildMessage ( EmailDTO datiMail ) throws MessagingException {
        EPollMimeMessage smtpMessage = new EPollMimeMessage ( mailSession );

        EmailConfigDTO config = getConfig ( datiMail.getIdEnte () );
        smtpMessage.setFrom ( new InternetAddress ( config.getIndirizzoPec () ) );
        smtpMessage.setReplyTo ( new javax.mail.Address [] { new InternetAddress ( config.getIndirizzoPec () ) } );

        smtpMessage.setRecipients ( Message.RecipientType.TO, listToAdress ( datiMail.getTo () ) );
        smtpMessage.setRecipients ( Message.RecipientType.CC, listToAdress ( datiMail.getCc () ) );
        smtpMessage.setSubject ( datiMail.getOggetto () );
        smtpMessage.setContent ( createBodyMultipart ( datiMail.getBody (), datiMail.getMimeType (), datiMail.getAllegati() ) );
        smtpMessage.saveChanges ();
        addHeaderToMessage ( smtpMessage, datiMail );
        return smtpMessage;
    }

    /**
     * Ribalta gli header dal DTO al messaggio email
     * 
     * @param message email da inviare
     * @param datiMail dto di appoggio
     */
    @SuppressWarnings ( "all" ) // Usato in quanto con spring 3.1.1 non è possibile labda senza le classi anonime.
    private void addHeaderToMessage ( EPollMimeMessage message, EmailDTO datiMail ) {
        Assert.notEmpty ( datiMail.getHeaders (), String.format ( ErrorMessages.G_PARAMETRO_OBBLIGATORIO_NON_SPECIFICATO, "Headers" ) );
        try {
            message.addHeader ( "X-TipoRicevuta", "sintetica" );
        } catch ( MessagingException e1 ) {
            LOGGER.warn ( "addHeaderToMessage", String.format ( ErrorMessages.E_ERRORE_HEADER_NON_RIBALTATO, "\"X-TipoRicevuta" ) );
        }
        datiMail.getHeaders ().forEach ( new BiConsumer<String, String> () {

            @Override
            public void accept ( String key, String value ) {
                try {
                    message.addHeader ( key, value );
                } catch ( MessagingException e ) {
                    LOGGER.warn ( "addHeaderToMessage", String.format ( ErrorMessages.E_ERRORE_HEADER_NON_RIBALTATO, key ) );
                }
            }
        } );
    }

    /**
     * Metodo che crea l'oggetto che rappresenta il body della mail
     * 
     * @param body email
     * @return Multipart oggetto body
     * @throws MessagingException errore in fase di creazione del body
     */
    private Multipart createBodyMultipart ( String body, MimeType mimeType, List<AllegatoDTO> allegati ) throws MessagingException {
    	String methodName = "createBodyMultipart";
        // definisco corpo del messaggio e allegati      
        Multipart multipart = new MimeMultipart ();
        // corpo del messaggio      
        MimeBodyPart mbp1 = new MimeBodyPart ();
        mbp1.setContent ( body, mimeType.getValue () );
        multipart.addBodyPart ( mbp1 );

        // Inserire qui le istruzioni per attaccare alla mail futuri allegati.
        if(allegati != null) {
	        for(AllegatoDTO allegato : allegati){
	        	MimeBodyPart attachment = new MimeBodyPart();
	        	DataSource source = new FileDataSource(allegato.getPathAllegato() + "/" + allegato.getNomeAllegato());
	            try {
					attachment.setDataHandler(new DataHandler(source));
					attachment.setFileName(allegato.getNomeAllegato());
		            multipart.addBodyPart(attachment);
				} catch (MessagingException e) {
					LOGGER.error(methodName, "errore durante l'aggiunta dell'allegato: " + e);
					throw e;
				}
	            
	        }
        }
        
        return multipart;
    }

    /**
     * Metodo che permette di creare gli oggetti Address per l'invio delle mail
     * 
     * @param adressList lista di indirizzi email
     * @return Address [] lista di oggetti email
     */
    private Address [] listToAdress ( List<String> adressList ) {
        List<Address> retAdess = new LinkedList<> ();
        if ( !CollectionUtils.isEmpty ( adressList ) ) {
            for ( String adress: adressList ) {
                try {
                    retAdess.add ( new InternetAddress ( adress ) );
                } catch ( AddressException e ) {
                    LOGGER.error ( "listToAdress", String.format ( ErrorMessages.E_ERRORE_PARSIFICAZIONE_INDIRIZZO_EMAIL, adress ), e );
                }
            }
        }
        return retAdess.toArray ( new Address [retAdess.size ()] );
    }

    @Override
    public boolean isConnected () {
        return connected;
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
        return EmailConfigDTO.builder ()
            .withPassword ( new String ( pecCfg.getSmtpPassword (), StandardCharsets.UTF_8 ) )
            .withServerHostnameIMAP ( pecCfg.getImapServerHostname () )
            .withServerHostnameSMTP ( pecCfg.getSmtpServerHostname () )
            .withUsername ( pecCfg.getSmtpUsername () )
            .withConnectedSMTP ( isConnected () )
            .withIndirizzoPec ( pecCfg.getIndirizzoPec () )
            .build ();
    }
}
