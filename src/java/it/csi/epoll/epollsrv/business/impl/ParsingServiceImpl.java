/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import it.csi.epoll.epollsrv.business.dto.ParsingPollOutputDTO;
import it.csi.epoll.epollsrv.business.dto.PreferenzaDaValidareDTO;
import it.csi.epoll.epollsrv.business.dto.StatoPollEnum;
import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.dto.email.MimeType;
import it.csi.epoll.epollsrv.business.dto.email.ParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.dto.email.PollParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.dto.email.PreferenzaEspressaParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.dto.email.PreferenzeEspresseParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.service.BodyPreferenzaService;
import it.csi.epoll.epollsrv.business.service.ElettoreService;
import it.csi.epoll.epollsrv.business.service.EmailServiceSMTP;
import it.csi.epoll.epollsrv.business.service.ParsingService;
import it.csi.epoll.epollsrv.business.service.PollService;
import it.csi.epoll.epollsrv.business.service.PreferenzaEspressaService;
import it.csi.epoll.epollsrv.business.service.PreferenzaService;
import it.csi.epoll.epollsrv.business.service.PresidenteService;
import it.csi.epoll.epollsrv.integration.domain.Elettore;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.Preferenza;
import it.csi.epoll.epollsrv.integration.domain.Presidente;
import it.csi.epoll.epollsrv.integration.domain.StatoPoll;
import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.ErrorMessages;
import it.csi.epoll.epollsrv.util.email.EmailParser;
import it.csi.epoll.epollsrv.util.email.template.EsitoNegativoNuovaVotazioneTemplate;
import it.csi.epoll.epollsrv.util.email.template.InvitoVotazioneTemplate;
import it.csi.epoll.epollsrv.util.email.template.InvitoVotazioneTemplateMultiPreferenza;
import it.csi.epoll.epollsrv.util.email.template.PollConverter;
import it.csi.epoll.epollsrv.util.exception.GenericParameterException;
import it.csi.epoll.epollsrv.util.exception.GenericParseErrorException;
import it.csi.epoll.epollsrv.util.exception.IllegalMailSubjectException;
import it.csi.epoll.epollsrv.util.exception.MissingRequiredSectionException;
import it.csi.epoll.epollsrv.util.exception.NotEnoughPreferenzeException;
import it.csi.epoll.epollsrv.util.exception.NotValidClosingException;
import it.csi.epoll.epollsrv.util.exception.NotValidDateException;
import it.csi.epoll.epollsrv.util.exception.UnexpectedSectionException;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 * Servizio principale di elaborazione dei messaggi
 */

@Service
@Transactional
public class ParsingServiceImpl implements ParsingService {

    private static final String CLASS_NAME = "ParsingServiceImpl";

    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_BUSINESS_LOG_CATEGORY, CLASS_NAME );

    private SimpleDateFormat sdf = new SimpleDateFormat ( "dd/MM/yyyy HH:mm" );

    @Autowired
    private PresidenteService presidenteService;

    @Autowired
    private PollService pollService;

    @Autowired
    private PreferenzaEspressaService preferenzaEspressaService;

    @Autowired
    private EmailServiceSMTP emailServiceSMTP;

    @Autowired
    private ElettoreService elettoreService;

    @Autowired
    private PreferenzaService preferenzaService;
    
    @Autowired
    private BodyPreferenzaService bodyPreferenzaService; 

    /**
     * Variabile usata per sincronizzare il conteggio dei voti. Trovate un metodo più efficiente se volte.
     */
    private static final Object prefrenzaObjectLock = new Object ();

    @Override
    @Async
    @Transactional ( propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class )
    public ParsedIncomingMessage parseIncomingMessage ( EmailDTO emailDTO )
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {

        final String methodName = "parseIncomingMessage";

        ParsedIncomingMessage parsedIncomingMessage = null;

        Assert.notNull ( emailDTO, ErrorMessages.P_ERRORE_MESSAGGIO_VUOTO );

        Assert.notNull ( emailDTO.getFrom (), ErrorMessages.P_ERRORE_NESSUN_MITTENTE_PRESENTE );

        Assert.notNull ( emailDTO.getReceivedDate (), ErrorMessages.P_DATA_RICEZIONE_MAIL_NON_TROVATA );

        Assert.hasText ( emailDTO.getOggetto (), ErrorMessages.P_ERRORE_NESSUN_OGGETTO_PRESENTE );

        String pollCode = emailDTO.getHeader ( Constants.HEADER_REPLY_TO_POLL_INVITATION );

        ParsingPollOutputDTO parsingPollOutput = null;

        if ( !StringUtils.hasText ( pollCode ) ) {
            parsingPollOutput = EmailParser.parsePollCodeFromSubject ( emailDTO.getOggetto () );
            Assert.notNull ( parsingPollOutput.getCodicePoll (), ErrorMessages.P_ERRORE_CODICE_POLL_NON_RICONOSCIUTO );
        }
        Poll pollFromDB = pollService.cercaPollPerCodice ( parsingPollOutput.getCodicePoll () );

        switch ( parsingPollOutput.getAzione () ) {

            case Constants.NUOVA_VOTAZIONE :
                Presidente presidente = presidenteService.cercaPresidenteDaEmail ( emailDTO.getFrom () );
                Assert.notNull ( presidente, String.format ( ErrorMessages.DB_PRESIDENTE_NON_PRESENTE_CON_SPECIFICA_EMAIL, emailDTO.getFrom () ) );

                try {
                parsedIncomingMessage = parseNewPoll ( presidente, emailDTO, parsingPollOutput.getCodicePoll () );

                } catch ( UnexpectedSectionException | MissingRequiredSectionException | GenericParseErrorException | NotValidDateException
                                | NotEnoughPreferenzeException | GenericParameterException e ) {
                    LOGGER.error ( methodName, "Errore durante il parsing di un nuovo poll", e );

                    String motivazione = "";

                    if ( e instanceof UnexpectedSectionException ) {
                        motivazione = String.format ( "Sezione non attesa: %s", ( (UnexpectedSectionException) e ).getSection () );
                    } else if ( e instanceof MissingRequiredSectionException ) {
                        motivazione = String.format ( "Sezione mancante: %s", ( (MissingRequiredSectionException) e ).getSection () );
                    } else if ( e instanceof GenericParseErrorException ) {
                        motivazione = "Errore di parsing del messaggio in arrivo";
                    } else if ( e instanceof NotValidDateException ) {
                        motivazione = ( (NotValidDateException) e ).getMessage ();
                    } else if ( e instanceof NotEnoughPreferenzeException ) {
                        motivazione = ( (NotEnoughPreferenzeException) e ).getMessage ();
                    } else if ( e instanceof GenericParameterException ) {
                        motivazione = ( (GenericParameterException) e ).getMessage ();
                    } else {
                        motivazione = "Errore generico";
                    }

                    LOGGER.info ( methodName, "Invio mail di risposta" );
                    EsitoNegativoNuovaVotazioneTemplate nuova = new EsitoNegativoNuovaVotazioneTemplate ();
                    nuova.setMotivazione ( motivazione );
                    emailServiceSMTP.connectMail ();
                    if ( !emailServiceSMTP.sendMail ( prepareEmailDTO ( presidente, nuova.creaContenutoEmail (),
                        String.format ( ErrorMessages.P_ERRORE_POLL_NON_CREATO, emailDTO.getOggetto () ) ) ) ) {
                        LOGGER.error ( methodName, ErrorMessages.E_ERRORE_INVIO_MAIL );
                    }
                    throw e;
                }

                break;
        case Constants.CHIUSURA_ANTICIPATA :
            presidente = presidenteService.cercaPresidenteDaEmail ( emailDTO.getFrom () );
            Assert.notNull ( presidente, String.format ( ErrorMessages.DB_PRESIDENTE_NON_PRESENTE_CON_SPECIFICA_EMAIL, emailDTO.getFrom () ) );

            try {
                parsedIncomingMessage = parseChiusuraAnticipata ( emailDTO, parsingPollOutput.getCodicePoll (), presidente );
            } catch ( NotValidClosingException e ) {

                LOGGER.info ( methodName, "Invio mail di risposta" );
                EsitoNegativoNuovaVotazioneTemplate nuova = new EsitoNegativoNuovaVotazioneTemplate ();
                nuova.setMotivazione ( ErrorMessages.P_ERRORE_CHIUSURA_NON_CONSENTITA );
                emailServiceSMTP.connectMail ();
                if ( !emailServiceSMTP.sendMail ( prepareEmailDTO ( presidente, nuova.creaContenutoEmail (),
                    String.format ( ErrorMessages.P_ERRORE_POLL_NON_CREATO, emailDTO.getOggetto () ) ) ) ) {
                    LOGGER.error ( methodName, ErrorMessages.E_ERRORE_INVIO_MAIL );
                }
               
            }
            break;

        case Constants.PREFERENZA_ESPRESSA :
            try {
                if ( pollFromDB.getEnte ().getAlgoritmoVotazione ().getCodAlgoritmo ().equals ( "SINGLE" ) ) {
                    parsedIncomingMessage = parsePreferenzaEspressa ( pollFromDB, emailDTO );
                } else if ( pollFromDB.getEnte ().getAlgoritmoVotazione ().getCodAlgoritmo ().equals ( "MULTI" ) ) {
                    parsedIncomingMessage = parsePreferenzeEspresse ( pollFromDB, emailDTO );
                }

            } catch ( Exception e ) {
                //:TODO gestire l'errore dando riscontro al votante o al presidente?
                LOGGER.error ( methodName, ErrorMessages.E_ERRORE_CONTEGGIO_EMAIL, e );
                throw e;
                }
                break;
        }

        Assert.notNull ( parsedIncomingMessage, ErrorMessages.P_ERRORE_MESSAGGIO_NON_ELABORATO );
        return parsedIncomingMessage;
    }

    /**
     * Prepara la mail di errore da inviare al presidente
     *
     * @param presidente
     * @param body
     * @param oggetto
     * @return
     */
    private EmailDTO prepareEmailDTO ( Presidente presidente, String body, String oggetto ) {
        EmailDTO email = new EmailDTO ( presidente.getEnte ().getId () );
        email.setTo ( Arrays.asList ( presidente.getEmail () ) );
        email.setBody ( body );
        email.setOggetto ( oggetto );
        email.setMimeType ( MimeType.TEXT_HTML );
        Map<String, String> headers = new LinkedHashMap<> ();
        headers.put ( Constants.HEADER_IDENTIFICATIVO_SISTEMA, "epoll" );
        email.setHeaders ( headers );
        return email;
    }

    private PollParsedIncomingMessage parseNewPoll ( Presidente presidente, EmailDTO emailDTO, String codicePoll )
                    throws UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException, NotValidDateException , NotEnoughPreferenzeException, GenericParameterException {

        final String methodName = "parseNewPoll";

        PollParsedIncomingMessage parsedMessage = new PollParsedIncomingMessage ();
        parsedMessage.setCodicePoll ( codicePoll );
        parsedMessage.setIsMittentePresidente ( true );
        parsedMessage.setMittente ( emailDTO.getFrom () );

        Assert.notNull ( emailDTO.getBody (), ErrorMessages.P_ERRORE_EMAIL_PRIVA_DI_CONTENUTO );
        
        
        
        Map<String, List<String>> parsedContent = null;
        
        //prova parse html e in caso di eccezione effettua parse text/plain
		try {
			parsedContent = EmailParser.parseEmailHTMLBody( emailDTO.getBody (), EmailParser.getDelimitersNewPoll () );
		} catch (Exception e) {
		    while(emailDTO.getBody().startsWith("\r\n")) {
              if (emailDTO.getBody().startsWith("\r\n")) {
                  emailDTO.setBody(emailDTO.getBody().substring(2, emailDTO.getBody().length() - 1));
              }
            }
          
            parsedContent = EmailParser.parseEmailBody ( emailDTO.getBody (), EmailParser.getDelimitersNewPoll () );
		}
		
// Commentato temporaneamente per fix mail html vista con mime TEXT/PLAIN.        
//        if ( MimeType.TEXT_PLAIN == emailDTO.getMimeType () ) {
//        	
//        	while(emailDTO.getBody().startsWith("\r\n")) {
//    			if (emailDTO.getBody().startsWith("\r\n")) {
//    				emailDTO.setBody(emailDTO.getBody().substring(2, emailDTO.getBody().length() - 1));
//    			}
//            }
//        	
//            parsedContent = EmailParser.parseEmailBody ( emailDTO.getBody (), EmailParser.getDelimitersNewPoll () );
//
//        } else if ( MimeType.TEXT_HTML == emailDTO.getMimeType () ) {
//
//            parsedContent = EmailParser.parseEmailHTMLBody ( emailDTO.getBody (), EmailParser.getDelimitersNewPoll () );
//            int placeholder = 0;
//        } else {
//            LOGGER.error ( methodName, ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
//            throw new IllegalStateException ( ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
//        }

        Assert.notNull ( parsedContent, ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );

        //creazione e salvataggio poll
        Poll poll = pollService.save ( populatePoll ( parsedContent, emailDTO, codicePoll, presidente ) );
        parsedMessage.setPoll ( poll );
        //salvataggio Elettore
        if ( !CollectionUtils.isEmpty ( poll.getElettoreList () ) ) {
            for ( Elettore elettore: poll.getElettoreList () ) {
                elettore.setPoll ( poll );
                elettoreService.save ( elettore );
            }
        }
        //salvataggion Preferenza
        if ( !CollectionUtils.isEmpty ( poll.getPreferenzaList () ) ) {
            for ( Preferenza preferenza: poll.getPreferenzaList () ) {
                preferenza.setPoll ( poll );
                preferenzaService.save ( preferenza );
            }
        }
        emailServiceSMTP.connectMail ();
        if(poll.getEnte().getAlgoritmoVotazione().getCodAlgoritmo().equals("SINGLE")) {
        	creaEmailPerVotanti ( emailDTO, poll );
        }else {
        	creaEmailPerVotantiMultiPreferenza(emailDTO, poll);
        }
        emailDTO.setNuovoPoll ( true );
        return parsedMessage;
    }

    private void creaEmailPerVotanti ( EmailDTO emailNuovoPoll, Poll pollCreato ) {

        String method = "creaEmailPerVotanti";

        EmailDTO mailToVotante = new EmailDTO ( emailNuovoPoll.getIdEnte () );

        InvitoVotazioneTemplate nuova = new InvitoVotazioneTemplate ();
        
        nuova.setPollDTO ( PollConverter.pollEntityToDTO ( pollCreato ) );

        Map<String, String> headers = new HashMap<> ();
        headers.put ( Constants.HEADER_REPLY_TO_POLL_INVITATION, pollCreato.getCodice () );

        mailToVotante.setBody ( nuova.creaContenutoEmail () );
        mailToVotante.setFrom ( null /* cosi' dice A */ );// settato in fase di invio.
        mailToVotante.setHeaders ( headers );
        mailToVotante.setMimeType ( MimeType.TEXT_HTML );
        mailToVotante.setOggetto ( String.format ( Constants.OGGETTO_MAIL_INVITO_VOTAZIONE, pollCreato.getOggetto (), pollCreato.getCodice () ) );

        for ( Elettore elettore: pollCreato.getElettoreList () ) {
            List<String> toList = new LinkedList<> ();
            toList.add ( elettore.getEmail () );
            mailToVotante.setTo ( toList );
            LOGGER.info ( method,
                String.format ( "Email di invito nuova votazione %s in corso di invio alla casella %s", pollCreato.getCodice (), elettore.getEmail () ) );

            if ( emailServiceSMTP.sendMail ( mailToVotante ) ) {
                LOGGER.info ( method,
                    String.format ( "Email di invito nuova votazione %s inviata alla casella %s", pollCreato.getCodice (), elettore.getEmail () ) );
            } else {
                LOGGER.error ( method,
                    String.format ( "Errore durante l'invio della email di invito nuova votazione %s verso la casella %s", pollCreato.getCodice (),
                        elettore.getEmail () ) );
            }
        }
    }
    
    private void creaEmailPerVotantiMultiPreferenza ( EmailDTO emailNuovoPoll, Poll pollCreato ) {

        String method = "creaEmailPerVotantiMultiPreferenza";

        EmailDTO mailToVotante = new EmailDTO ( emailNuovoPoll.getIdEnte () );

        InvitoVotazioneTemplateMultiPreferenza nuova = new InvitoVotazioneTemplateMultiPreferenza ();
        
        nuova.setPollDTO ( PollConverter.pollEntityToDTO ( pollCreato ) );

        Map<String, String> headers = new HashMap<> ();
        headers.put ( Constants.HEADER_REPLY_TO_POLL_INVITATION, pollCreato.getCodice () );

        mailToVotante.setBody ( nuova.creaContenutoEmail () );
        mailToVotante.setFrom ( null /* cosi' dice A */ );// settato in fase di invio.
        mailToVotante.setHeaders ( headers );
        mailToVotante.setMimeType ( MimeType.TEXT_HTML );
        mailToVotante.setOggetto ( String.format ( Constants.OGGETTO_MAIL_INVITO_VOTAZIONE, pollCreato.getOggetto (), pollCreato.getCodice () ) );

        for ( Elettore elettore: pollCreato.getElettoreList () ) {
            List<String> toList = new LinkedList<> ();
            toList.add ( elettore.getEmail () );
            mailToVotante.setTo ( toList );
            LOGGER.info ( method,
                String.format ( "Email di invito nuova votazione %s in corso di invio alla casella %s", pollCreato.getCodice (), elettore.getEmail () ) );

            if ( emailServiceSMTP.sendMail ( mailToVotante ) ) {
                LOGGER.info ( method,
                    String.format ( "Email di invito nuova votazione %s inviata alla casella %s", pollCreato.getCodice (), elettore.getEmail () ) );
            } else {
                LOGGER.error ( method,
                    String.format ( "Errore durante l'invio della email di invito nuova votazione %s verso la casella %s", pollCreato.getCodice (),
                        elettore.getEmail () ) );
            }
        }
    }

    private Poll populatePoll ( Map<String, List<String>> parsedContent, EmailDTO emailDTO, String codicePoll, Presidente presidente )
                    throws NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {

        final String methodName = "parseNewPoll";

        LOGGER.begin ( CLASS_NAME, methodName );

        Poll poll = new Poll ();

        poll.setDtCreazione ( emailDTO.getSentDate () );
    	Date inizioValidita =null;
        Date fineValidita = null;
        Boolean isVotoIstantaneo = false;
  
        if(CollectionUtils.isEmpty (parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_INIZIO ))) {
        	
        	isVotoIstantaneo = true;
        	inizioValidita = new java.util.Date();
        	
        }else {
        	try {
                inizioValidita = sdf.parse ( parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_INIZIO ).get ( 0 ) );
        	}catch (ParseException e) {
        		 throw new NotValidDateException ( ErrorMessages.E_ERRORE_DATA_INIZIO_VALIDITA );
        	}       	
        }
           	
    	try {	            
            fineValidita = sdf.parse ( parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_FINE ).get ( 0 ) );
    	}catch (ParseException e) {
    		 throw new NotValidDateException ( ErrorMessages.E_ERRORE_DATA_FINE_VALIDITA );
    	}

        validaData ( inizioValidita, fineValidita, isVotoIstantaneo );

        poll.setDtInizioValidita ( inizioValidita );
        poll.setDtFineValidita ( fineValidita );
        if (codicePoll.length()>50) {
            throw new GenericParameterException (ErrorMessages.E_ERRORE_CODICE_VOTAZIONE_TROPPO_LUNGO);
        }       	
        

        if ( !CollectionUtils.isEmpty ( parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_NOTIFICHE ) ) ) {
            try {
                poll.setFrequenzaAggiornamento ( Long.parseLong ( parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_NOTIFICHE ).get ( 0 ).trim () ) );
                poll.setDtUltimaNotifica ( new Date () );
            } catch ( NumberFormatException e ) {
                throw new GenericParameterException ( ErrorMessages.E_ERRORE_INVIO_NOTIFICHE );
            }

        }


        poll.setCodice ( codicePoll );
        poll.setEnte ( presidente.getEnte () );
        poll.setOggetto ( parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_OGGETTO ).get ( 0 ).trim () );
        poll.setPresidente ( presidente );
        poll.setTesto ( String.join ( "\r", parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_TESTO ) ) );
        StatoPollEnum statoPollCreato = StatoPollEnum.CREATO;
        StatoPoll statoPoll = new StatoPoll ();
        statoPoll.setId ( statoPollCreato.getCodStato () );
        statoPoll.setDescrizione ( statoPollCreato.getDescrizione () );
        poll.setStatoPoll ( statoPoll );

        List<Preferenza> preferenze = new LinkedList<> ();
        Pattern pattern = Pattern.compile("^\\S+");
        Matcher matcher;
        for ( String elem: parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_PREFERENZE ) ) {
            if(elem != null) {
            	matcher = pattern.matcher(elem);
            	Preferenza preferenza = new Preferenza ();
            	while(matcher.find()) {
            		String codPreferenza = matcher.group(0).trim();
            		if (codPreferenza.length()>20) {
                        throw new GenericParameterException (ErrorMessages.E_ERRORE_CODICE_PREFERENZA_TROPPO_LUNGO);
                    }
            		preferenza.setCodPreferenza(codPreferenza);
            		preferenza.setPreferenza ( Arrays.stream(elem.split("^\\S+")).collect(Collectors.toList()).get(1));
            		preferenze.add ( preferenza );
            	}
            }
        }
        

        List<Elettore> elettori = new LinkedList<> ();
        for ( String elem: parsedContent.get ( EmailParser.EMAIL_SECTION_DELIMITER_ELETTORI ) ) {
            Elettore elettore = new Elettore ();
            validateElettore ( elem );
            elettore.setCognomeNome ( elem.substring ( 0, elem.indexOf ( '(' ) ).trim () );
            elettore.setEmail ( elem.substring ( elem.indexOf ( '(' ) + 1, elem.length () - 1 ).trim () );
            elettori.add ( elettore );
        }
        poll.setElettoreList ( elettori );
        
        int numeroMaxPreferenze = 1;
        if( parsedContent.containsKey(EmailParser.EMAIL_SECTION_DELIMITER_NUMERO_MAX_PREFERENZE) ) {
        	if ( presidente.getEnte().getAlgoritmoVotazione().getCodAlgoritmo().equals("MULTI") ) {
            	String numMaxPrefs = parsedContent.get(EmailParser.EMAIL_SECTION_DELIMITER_NUMERO_MAX_PREFERENZE).get(0).trim();
            	try {
            		numeroMaxPreferenze = Integer.parseInt(numMaxPrefs);
            	} catch (NumberFormatException nfe) {
                    LOGGER.error ( methodName, ErrorMessages.P_ERRORE_FORMATO_NON_VALIDO, nfe );
                    throw new IllegalStateException ( ErrorMessages.P_ERRORE_FORMATO_NON_VALIDO );
            	}
        	} else {
                LOGGER.error ( methodName, ErrorMessages.P_ERRORE_PARAMETRO_NUM_MAX_NON_AMMESSO );
                throw new IllegalStateException ( ErrorMessages.P_ERRORE_PARAMETRO_NUM_MAX_NON_AMMESSO );
        	}
        }
        poll.setPreferenzeDaEsprimere( numeroMaxPreferenze );
        if(numeroMaxPreferenze > preferenze.size()) {
        	LOGGER.error(methodName, ErrorMessages.P_ERRORE_QUANTITA_PREFERENZE_INSUFFICIENTE);
        	throw new NotEnoughPreferenzeException(ErrorMessages.P_ERRORE_QUANTITA_PREFERENZE_INSUFFICIENTE);
        } else if(numeroMaxPreferenze < 1){
        	LOGGER.error(methodName, ErrorMessages.P_NUMERO_PREFERENZE_NON_VALIDO);
        	throw new GenericParameterException(ErrorMessages.P_NUMERO_PREFERENZE_NON_VALIDO);
        }else {
        	poll.setPreferenzaList(preferenze);
        }

        return poll;
    }

    private void validateElettore ( String elettore ) {
        Assert.hasText ( elettore, ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
        boolean valid = ( elettore.indexOf ( '(' ) == elettore.lastIndexOf ( '(' ) ) && ( elettore.indexOf ( ')' ) == elettore.lastIndexOf ( ')' ) )
            && ( ( elettore.indexOf ( ')' ) - elettore.indexOf ( '(' ) ) > 1 );
        Assert.isTrue ( valid, ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
    }
    
    @Override
    public PreferenzaEspressaParsedIncomingMessage parsePreferenzaEspressa ( Poll existingPoll, EmailDTO emailDTO ) {

        final String methodName = "parsePreferenzaEspressa";

        LOGGER.begin ( CLASS_NAME, methodName );

        PreferenzaEspressaParsedIncomingMessage parsedMessage = new PreferenzaEspressaParsedIncomingMessage ();
        parsedMessage.setCodicePoll ( existingPoll.getCodice () );
        parsedMessage.setIsMittentePresidente ( emailDTO.getFrom ().equalsIgnoreCase ( existingPoll.getPresidente ().getEmail () ) );
        parsedMessage.setMittente ( emailDTO.getFrom () );

        // inserita gestione votazione con scheda bianca
        String preferenzaString = Constants.BLANK;
        
        //12345
        
        
        if ( StringUtils.hasText ( emailDTO.getBody () ) ) {
            // inserita per gestire le votazioni con scheda bianca
            LOGGER.info ( methodName, "Valore body email: " + emailDTO.getBody () );
            // :TODO da rimettere a DEBUG il prima possibile.
            LOGGER.info ( methodName, "body email: " + emailDTO.getBody () );
            if ( MimeType.TEXT_PLAIN == emailDTO.getMimeType () ) {
                preferenzaString = ( emailDTO.getBody () ).trim ();
            } else if ( MimeType.TEXT_HTML == emailDTO.getMimeType () ) {
                preferenzaString = EmailParser.removeAllHTMLTags ( ( emailDTO.getBody () ).trim () );
            } else {
                LOGGER.error ( methodName, ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
                throw new IllegalStateException ( ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
            }

            LOGGER.info(methodName, "Valore preferenza pre split: " + preferenzaString);
            
            String [] risSplit = StringUtils.split ( preferenzaString, "\r\n" );
            if ( null != risSplit ) {
                if ( StringUtils.hasText ( risSplit [0] ) ) {
                    preferenzaString = StringUtils.trimAllWhitespace ( risSplit [0] );
                } else {
                    preferenzaString = risSplit [0];
                }
            }
            LOGGER.info(methodName, "Valore preferenza post split: " + preferenzaString);
        }

        boolean preferenzaValida = validaPreferenza ( preferenzaString, existingPoll );
        boolean dataPreferenzaValida = preferenzaInDataValiditaRange( emailDTO.getReceivedDate (), existingPoll );
        preferenzaValida = preferenzaValida && dataPreferenzaValida;
        // valida l'elettore e lo aggiorna.
        Elettore elettoreAmmesso = elettoreService.cercaElettorePerIdPollEdEmail ( existingPoll.getId (), emailDTO.getFrom () );
        Assert.notNull ( elettoreAmmesso, ErrorMessages.DB_ELETTORE_NON_AMMESSO );
        elettoreAmmesso.setDtDataVotazione ( emailDTO.getReceivedDate () );
        elettoreService.save ( elettoreAmmesso );
        if( dataPreferenzaValida ) {
        	bodyPreferenzaService.salvaPreferenza ( preferenzaString, existingPoll );
        }
        synchronized ( prefrenzaObjectLock ) {
            // salvo oppure incremento la preferenza espressa in una transazione separata. Lasciarlo come ultima istruzione
            parsedMessage.setPreferenzaEspressa ( preferenzaEspressaService.salvaPreferenza ( existingPoll, preferenzaValida, preferenzaString) );
        }

        LOGGER.end ( CLASS_NAME, methodName );

        return parsedMessage;
    }
    
    @Override
    public PreferenzeEspresseParsedIncomingMessage parsePreferenzeEspresse( Poll existingPoll, EmailDTO emailDTO ) {
    	final String methodName = "parsePreferenzaEspressa";

        LOGGER.begin ( CLASS_NAME, methodName );
        
        PreferenzeEspresseParsedIncomingMessage parsedMessage = new PreferenzeEspresseParsedIncomingMessage();
        parsedMessage.setCodicePoll ( existingPoll.getCodice () );
        parsedMessage.setIsMittentePresidente ( emailDTO.getFrom ().equalsIgnoreCase ( existingPoll.getPresidente ().getEmail () ) );
        parsedMessage.setMittente ( emailDTO.getFrom () );
        
        String preferenzeString = Constants.BLANK;
        List<String> preferenzeList = new LinkedList<>();
        
        if ( StringUtils.hasText ( emailDTO.getBody () ) ) {
            // inserita per gestire le votazioni con scheda bianca

            if ( MimeType.TEXT_PLAIN == emailDTO.getMimeType () ) {

                preferenzeString = ( emailDTO.getBody () ).trim ();
                

            } else if ( MimeType.TEXT_HTML == emailDTO.getMimeType () ) {

                preferenzeString = EmailParser.removeAllHTMLTags ( ( emailDTO.getBody () ).trim () );

            } else {
                LOGGER.error ( methodName, ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
                throw new IllegalStateException ( ErrorMessages.P_ERRORE_CONTENUTO_NON_RICONOSCIUTO );
            }
            // Da migliorare con check
            
            if(StringUtils.hasText(preferenzeString)) {
            	 while(preferenzeString.startsWith("\r\n")) {
                     if (preferenzeString.startsWith("\r\n")) {
                    	preferenzeString =  preferenzeString.substring( 2 );
                     }
                   }
            	preferenzeString = StringUtils.split ( preferenzeString, "\r\n" ) [0];
            	preferenzeList = estraiPreferenze(preferenzeString);
            } else {
            	LOGGER.info(methodName, ErrorMessages.P_SCHEDA_BIANCA);
            	preferenzeString = " ";
            	preferenzeList.add(preferenzeString);
            }
            for(String preferenza : preferenzeList) {
            	LOGGER.info(methodName, "Valore preferenza: " + preferenza);
            }
            
        }else {
        	LOGGER.info(methodName, ErrorMessages.P_SCHEDA_BIANCA);
        	preferenzeString = " ";
        	preferenzeList.add(preferenzeString);
        }
        
        List<PreferenzaDaValidareDTO> preferenzeDaValidare = Collections.emptyList();
        boolean dataInRange = true;
        if(!preferenzeList.isEmpty () ) {
        	preferenzeDaValidare = validaPreferenze ( preferenzeList, existingPoll );
        	
            dataInRange = preferenzaInDataValiditaRange ( emailDTO.getReceivedDate(), existingPoll );
        	for(PreferenzaDaValidareDTO pref : preferenzeDaValidare) {
        		pref.setDataOraValida(dataInRange);
        		if(!dataInRange) {
        			pref.setValida(false);
        		}
        	}
        }
        if(!preferenzeDaValidare.isEmpty()) {
	     	// valida l'elettore e lo aggiorna.
	        Elettore elettoreAmmesso = elettoreService.cercaElettorePerIdPollEdEmail ( existingPoll.getId (), emailDTO.getFrom () );
	        Assert.notNull ( elettoreAmmesso, ErrorMessages.DB_ELETTORE_NON_AMMESSO );
	        elettoreAmmesso.setDtDataVotazione ( emailDTO.getReceivedDate () );
	        elettoreService.save ( elettoreAmmesso );
	        if( dataInRange ) {
	        	bodyPreferenzaService.salvaPreferenza ( preferenzeString, existingPoll );
	        }
	        synchronized ( prefrenzaObjectLock ) {
	            // salvo oppure incremento la preferenza espressa in una transazione separata. Lasciarlo come ultima istruzione
	            parsedMessage.setPreferenzeEspresse ( preferenzaEspressaService.salvaPreferenze ( existingPoll, preferenzeDaValidare) );
	        }
        } else {
        	LOGGER.error(methodName, ErrorMessages.P_VOTO_NON_VALIDO);
        }
        
        LOGGER.end ( CLASS_NAME, methodName );
        
        return parsedMessage;
    }
    
    private List<String> estraiPreferenze (String preferenze) {
    	Pattern regExpPattern = Pattern.compile("\\s*([0-9a-zA-Z]+)[\\,\\-\\;\\s]*");
        Matcher matcher = regExpPattern.matcher(preferenze);
        
        Set<String> preferenzeEstratte = new HashSet<> ();
        
        while (matcher.find ()) {
            preferenzeEstratte.add ( matcher.group (1) );
        }        
        
        return new ArrayList<String>(preferenzeEstratte);
    }
    /*
     * Una preferenza e' valida se viene ricevuta nel range di validita' del poll ed e' compresa nella lista di quelle definite in fase di creazione della
     * votazione
     */
    private boolean validaPreferenza ( String preferenzaString, Poll existingPoll ) {
        boolean valida = false;

        if ( !StringUtils.hasText ( preferenzaString ) ) {
            // inserita per gestire le votazioni con scheda bianca
            valida = true;
        } else {
            if ( null != existingPoll.getPreferenzaList () ) {
                for ( Preferenza preferenza: existingPoll.getPreferenzaList () ) {
                    if ( preferenza.getCodPreferenza().equalsIgnoreCase ( preferenzaString ) ) {
                        valida = true;
                        break;
                    }
                }
            }
        }

        return valida;
    }
    
    /*
     * Una preferenza e' valida se viene ricevuta nel range di validita' del poll ed e' compresa nella lista di quelle definite in fase di creazione della
     * votazione
     */
    private List<PreferenzaDaValidareDTO> validaPreferenze ( List<String> preferenzeList, Poll existingPoll ) {
    	
        boolean valida = true;
        boolean prefInPool=false;
        int valide=0;
        
        List<PreferenzaDaValidareDTO> preferenzeValidate = new LinkedList<>();
        if(!preferenzeList.isEmpty()) {
        	if(1 == preferenzeList.size()) { // PRIMA PREFERENZA
        		PreferenzaDaValidareDTO preferenza = new PreferenzaDaValidareDTO();
        		if(StringUtils.hasText(preferenzeList.get(0))) {
        			preferenza.setPreferenza(preferenzeList.get(0));
        			prefInPool=preferenzaInPreferenzePoll(preferenzeList.get(0) , existingPoll.getPreferenzaList());
        			if (prefInPool) {
        				valide++;
        				if(valide <= existingPoll.getPreferenzeDaEsprimere()) {
                            preferenza.setValida(true);
                        }
        			}
        			
        			valida = preferenza.isValida();
        		} else {
        			preferenza.setPreferenza("");
        			valide++;
        			if(valide <= existingPoll.getPreferenzeDaEsprimere()) {
                        preferenza.setValida(true);
                    }
        			
        		}
        		preferenzeValidate.add(preferenza);
        	} else { // DALLA SECONDA PREFERENZA IN POI
		        for(String preferenzaString : preferenzeList) {
	            	PreferenzaDaValidareDTO preferenza = new PreferenzaDaValidareDTO();
	            	preferenza.setPreferenza(preferenzaString);
	            	prefInPool=preferenzaInPreferenzePoll(preferenzaString , existingPoll.getPreferenzaList()) ;
        			if (prefInPool) {
        				//controllo se la preferenza per ora valida  ha un valore gia' votato.
	                   	 boolean giaVotata=false;
	                   	 for ( PreferenzaDaValidareDTO prefGiaValia: preferenzeValidate ) {
                            if ( preferenza.getPreferenza ().equalsIgnoreCase ( prefGiaValia.getPreferenza() ) ) {
                                giaVotata=true;
                            }
                        }
	                   	                  	 
	                   	 if (!giaVotata) {
	                   		valide++;
	        				if(valide <= existingPoll.getPreferenzeDaEsprimere()) {
                                preferenza.setValida(true);
                            }
	                   	 }   
        				
        			}
	                preferenzeValidate.add(preferenza);       
	                valida = valida && preferenza.isValida();
			    }
        	}  
        }
        
        
//        if( valida && ( preferenzeList.size() > existingPoll.getPreferenzeDaEsprimere() )) {
//        	valida = false;
//    	}
//        if( !valida ) {
//    		for(PreferenzaDaValidareDTO preferenza : preferenzeValidate) {
//    			preferenza.setValida(false);
//    		}
//        }
        
        return preferenzeValidate;
    }
    
    private boolean preferenzaInDataValiditaRange ( Date receivedDate, Poll existingPoll ) {
    	boolean valida = true;
    	
    	if ( ( null != receivedDate ) && ( null != existingPoll.getDtInizioValidita () ) && ( null != existingPoll.getDtFineValidita () ) ) {
            LocalDateTime sentLocalDateTime = receivedDate.toInstant ()
                .atZone ( ZoneId.systemDefault () )
                .toLocalDateTime ();

            LocalDateTime inizioValiditaLocalDateTime = existingPoll.getDtInizioValidita ().toInstant ()
                .atZone ( ZoneId.systemDefault () )
                .toLocalDateTime ();

            LocalDateTime fineValiditaLocalDateTime = existingPoll.getDtFineValidita ().toInstant ()
                .atZone ( ZoneId.systemDefault () )
                .toLocalDateTime ();

            valida = ( ChronoUnit.MILLIS.between ( inizioValiditaLocalDateTime, sentLocalDateTime ) >= 0 )
                && ( ChronoUnit.MILLIS.between ( sentLocalDateTime, fineValiditaLocalDateTime ) > 0 );

        }
    	
    	return valida;
    }

    private boolean preferenzaInPreferenzePoll(String preferenzaString, List<Preferenza> preferenzePoll) {
    	for(Preferenza preferenza : preferenzePoll) {
    		if(preferenzaString.equalsIgnoreCase(preferenza.getCodPreferenza())) {
    			return true;
    		}
    	}
		return false;
	}

	/**
     * Metodo per validare le date di inizio e fine votazione
     * 
     * @param inizio e' la data di inizio validita' per la votazione
     * @param fine e' la data di fine validita' della validazione
     * @param isVotoIstantaneo e' un flag che indica di scartare il controllo sulla data inizio se viene utilizzata la modalita di voto istantaneo
     * @throws NotValidDateException restituisce un errore nel caso in cui la data di inizio validita e' precedente a quella del sistema o nel caso in cui la
     *             data di fine validita e' precedente a quella di inizio validita' o di sistema
     */
    private void validaData ( Date inizio, Date fine, Boolean isVotoIstantaneo ) throws NotValidDateException {
        LocalDateTime now = LocalDateTime.now ();

        LocalDateTime inizioValidita = inizio.toInstant ()
            .atZone ( ZoneId.systemDefault () )
            .toLocalDateTime ();

        LocalDateTime fineValidita = fine.toInstant ()
            .atZone ( ZoneId.systemDefault () )
            .toLocalDateTime ();

        if ( !isVotoIstantaneo ) {
        	
        	 if ( inizioValidita.isBefore ( now ) ) {
                 throw new NotValidDateException ( ErrorMessages.E_ERRORE_DATA_INIZIO_VALIDITA );
             }        	
        }
       
        if ( fineValidita.isBefore ( inizioValidita ) || fineValidita.isBefore ( now ) ) {
            throw new NotValidDateException ( ErrorMessages.E_ERRORE_DATA_FINE_VALIDITA );
        }

    }

    private PollParsedIncomingMessage parseChiusuraAnticipata ( EmailDTO emailDTO, String codicePollDaChiudere, Presidente presidente )
                    throws NotValidClosingException {

        final String methodName = "parseChiusuraAnticipata";

        PollParsedIncomingMessage parsedMessage = new PollParsedIncomingMessage ();
        parsedMessage.setCodicePoll ( codicePollDaChiudere );
        parsedMessage.setIsMittentePresidente ( true );
        parsedMessage.setMittente ( emailDTO.getFrom () );

        //recupero il poll che si vuole chiudere
        Poll pollFromDB = pollService.cercaPollPerCodice ( codicePollDaChiudere );
        Assert.notNull ( pollFromDB, String.format ( ErrorMessages.E_ERRORE_CHIUSURA_ANTICIPATA_POLL_MANCANTE, codicePollDaChiudere ) );
        
        if ( pollFromDB.getStatoPoll ().getDescrizione ().equals ( StatoPollEnum.CREATO.getDescrizione () ) ) {

            pollFromDB.setDtFineValidita ( new Date () );
            pollService.save ( pollFromDB );

        } else {
            LOGGER.info ( methodName,
                String.format ( "Il poll codice : %s  risulta essere scrutinato", codicePollDaChiudere ) );
            throw new NotValidClosingException ( ErrorMessages.P_ERRORE_CHIUSURA_NON_CONSENTITA );
        }
        
        return parsedMessage;
    }
}
