/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.schedule.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.RisultatiScrutinioPollDTO;
import it.csi.epoll.epollsrv.business.dto.email.AllegatoDTO;
import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.dto.email.MimeType;
import it.csi.epoll.epollsrv.business.service.BodyPreferenzaService;
import it.csi.epoll.epollsrv.business.service.EmailServiceSMTP;
import it.csi.epoll.epollsrv.business.service.PollService;
import it.csi.epoll.epollsrv.integration.domain.BodyPreferenza;
import it.csi.epoll.epollsrv.schedule.EPollScrutinioScheduler;
import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.email.template.ScrutinioVotazioneTemplate;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 * @author lfantini Scheduler che si occupa di cercare e scrutinare i poll terminati. Al termine dello scrutinio manda invia le relative mail con i risultati
 */

@Service
@Transactional
public class EPollScrutinoSchedulerImpl implements EPollScrutinioScheduler {

    private static final long RATE_MILLIS = 120000L; // 2 minuti

    private static final String CLASS_NAME = "EPollScrutinoSchedulerImpl";
    
    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_SCHEDULER_LOG_CATEGORY, CLASS_NAME );

    @Autowired
    private PollService pollService;
    
    @Autowired
    private BodyPreferenzaService bodyPreferenzaService;
    
    @Autowired
    private EmailServiceSMTP emailServiceSMTP;

    @Async
    @Scheduled ( fixedRate = RATE_MILLIS )
    @Transactional ( propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class )
    @Override
    public void execute () {
        final String methodName = "execute";
        try {
            doExecute ();
        } catch ( Exception e ) {
            LOGGER.error ( methodName, "Errore non gestito nell'esecuzione del batch " + this.getClass ().getSimpleName (), e );
        }
    }

    @SuppressWarnings ( "unchecked" )
    private <T> List<T> shuffleArray ( List<T> listToShuffle ) {
        // Implementing FisherYates shuffle
        Object [] array = listToShuffle.toArray ();

        Random rnd = ThreadLocalRandom.current ();
        for ( int i = array.length - 1; i > 0; i-- ) {
            int index = rnd.nextInt ( i + 1 );

            Object a = array [index];
            array [index] = array [i];
            array [i] = a;
        }

        return (List<T>) Arrays.asList ( array );
    }

    private void doExecute() throws IOException{
        final String methodName = "execute";

        LOGGER.info ( methodName, "Searching for new polls" );

        List<RisultatiScrutinioPollDTO> risultatiPollList = pollService.cercaEScrutinaPoll ();

        if ( ( null != risultatiPollList ) && !risultatiPollList.isEmpty () ) {
            for ( RisultatiScrutinioPollDTO risultatiPoll: risultatiPollList ) {

                LOGGER.info ( methodName, "Costruisco l'email da inviare" );
                ScrutinioVotazioneTemplate scrutinioVotazioneTemplate = new ScrutinioVotazioneTemplate ( risultatiPoll );
                String contenuto = scrutinioVotazioneTemplate.creaContenutoEmail ();
                
                List<BodyPreferenza> bodyPreferenzaList = bodyPreferenzaService.cercaPreferenzeByPollId ( risultatiPoll.getPollDTO ().getCodice () );
                
                bodyPreferenzaList = shuffleArray ( bodyPreferenzaList );

                List<AllegatoDTO> allegati = new LinkedList<>();
                if(!bodyPreferenzaList.isEmpty()) {
                    for ( BodyPreferenza bp: bodyPreferenzaList ) {
                        AllegatoDTO allegatoBodyPreferenza = new AllegatoDTO ();
                        String fileName = "Preferenza " + UUID.randomUUID ().toString ();
                        File file = null;
                        try {
                            file = File.createTempFile ( fileName, ".txt" );
                        } catch ( IOException ioe ) {
                            LOGGER.error ( methodName, "Errore durante la creazione del file: " + ioe, ioe );
                            throw ioe;
                        }
                        try ( FileOutputStream outStream = new FileOutputStream ( file ); PrintWriter pw = new PrintWriter ( outStream ); ) {
                            pw.println ( bp.toStringPreferenza () );
                        } catch ( Exception e ) {
                            LOGGER.error ( methodName, "errore durante la scrittura del file: " + e, e );
                            throw e;
                        }
                        allegatoBodyPreferenza.setAllegato ( bp.toStringPreferenza ().getBytes () );
                        allegatoBodyPreferenza.setNomeAllegato ( file.getName () );
                        allegatoBodyPreferenza.setPathAllegato ( file.getParent () );
                        allegatoBodyPreferenza.setTipoMIME ( MimeType.TEXT_PLAIN.getValue () );
                        allegati.add ( allegatoBodyPreferenza );
                        file.deleteOnExit ();
                    }
                }     
                EmailDTO email = new EmailDTO ( risultatiPoll.getPollDTO ().getEnte ().getId () );
                Map<String, String> headers = new LinkedHashMap<> ();
                headers.put ( Constants.HEADER_IDENTIFICATIVO_SISTEMA, "epoll" );
                
                email.setTo ( Arrays.asList ( risultatiPoll.getPollDTO ().getPresidente ().getEmail () ) );
                email.setBody ( contenuto );
                email.setOggetto ( String.format ( Constants.OGGETTO_RISULTATI, risultatiPoll.getPollDTO ().getCodice () ) );
                email.setMimeType ( MimeType.TEXT_HTML );
                email.setHeaders ( headers );
                email.setAllegati(allegati);
                

                LOGGER.info ( methodName,
                    "Invio email per i risultati dello scrutinio della votazione con codice: " + risultatiPoll.getPollDTO ().getCodice () );

                boolean esitoEmail = emailServiceSMTP.sendMail ( email );
                if ( esitoEmail ) {
                    LOGGER.info ( methodName, "Email inviata" );
                } else {
                    LOGGER.info ( methodName, "Email non inviata" );
                }

            }
        } else {
            LOGGER.info ( methodName, String.format ( "No eligible polls found, new run in %d seconds", RATE_MILLIS / 1000 ) );
        }
    }

}
