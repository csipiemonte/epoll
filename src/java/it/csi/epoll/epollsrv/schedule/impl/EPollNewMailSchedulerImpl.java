/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.schedule.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.service.EmailServiceIMAP;
import it.csi.epoll.epollsrv.business.service.EnteService;
import it.csi.epoll.epollsrv.business.service.ParsingService;
import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.schedule.EPollNewMailScheduler;
import it.csi.epoll.epollsrv.util.email.ricerca.SearchTermRead;
import it.csi.epoll.epollsrv.util.exception.GenericParseErrorException;
import it.csi.epoll.epollsrv.util.exception.IllegalMailSubjectException;
import it.csi.epoll.epollsrv.util.exception.MissingRequiredSectionException;
import it.csi.epoll.epollsrv.util.exception.UnexpectedSectionException;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 * @author lfantini Scheduler che legge tutte le mail nuove e agisce di conseguenza:
 *
 *         * creando un nuovo poll * effettuando le operazioni di voto * rispondendo al mittente nel caso in cui la mail non rispetti il formato desiderato
 */

@Service
@Transactional
public class EPollNewMailSchedulerImpl implements EPollNewMailScheduler {

    @Autowired
    private EmailServiceIMAP eMailService;

    @Autowired
    private EnteService enteService;

    @Autowired
    private ParsingService parsingService;

    private static final long RATE_MILLIS = 60000L; // 3 minuti

    private static final String CLASS_NAME = "EPollNewMailSchedulerImpl";

    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_SCHEDULER_LOG_CATEGORY, CLASS_NAME );

    @Override
    @Scheduled ( fixedRate = RATE_MILLIS )
    @Transactional ( readOnly = true )
    public void execute () {

        final String methodName = "execute";

        LOGGER.info ( methodName, "Searching for new messages" );

        //abora: inserimento logica multiente. Sarebbe meglio gestirla con un altro service per poter gestire una singola transazione per ogni ente.
        //Per adesso va bene un for con una transazione unica per tutte le caselle email
        List<Ente> entiDaElaborare = enteService.findAll ();
        if ( !CollectionUtils.isEmpty ( entiDaElaborare ) ) {
            for ( Ente ente: entiDaElaborare ) {
                try {
                    eMailService.connectMail ();
                    if ( eMailService.isConnected () ) {
                        List<EmailDTO> foundMessages = eMailService.searchMail ( new SearchTermRead ( ente.getId () ) );
                        if ( !CollectionUtils.isEmpty ( foundMessages ) ) {
                            Collections.sort ( foundMessages, new Comparator<EmailDTO> () {

                                @Override
                                public int compare ( EmailDTO o1, EmailDTO o2 ) {
                                    return o1.getReceivedDate ().compareTo ( o2.getReceivedDate () );
                                }
                            } );
                            for ( EmailDTO message: foundMessages ) {
                                try {
                                    parsingService.parseIncomingMessage ( message );
                                } catch ( IllegalMailSubjectException | IOException | UnexpectedSectionException | MissingRequiredSectionException
                                                | GenericParseErrorException e ) {

                                    // TODO Gestione degli errori!

                                    LOGGER.error ( methodName, "Parsing error", e );
                                } catch ( Exception me ) {
                                    LOGGER.error ( methodName, "Errore in fase di elaborazione del messaggio: " + message.toString (), me );
                                }
                                if ( !message.isNuovoPoll () ) {
                                    eMailService.deleteMail ( message.getEmailColl () );
                                }
                            }
                        } else {
                            LOGGER.info ( methodName, String.format ( "No messages found, next execution in %d seconds", ( RATE_MILLIS / 1000 ) ) );
                        }
                    } else {
                        LOGGER.info ( methodName, "Connessione interrotta" );
                    }
                } finally {
                    eMailService.disconnectMail ( ente.getId () );
                }
            }
        } else {
            LOGGER.info ( "methodName", "NESSUN ENTE TROVATO" );
        }
    }

    @Override
    //    @Scheduled ( fixedRate = 300000 )
    @SuppressWarnings ( "all" )
    public void test () {
        LOGGER.info ( "test", "test start" );
        LOGGER.info ( "test", "Sleeping for 10 seconds in a method with fixedRate of 3 seconds" );
        try {
            Thread.sleep ( 10000 );
        } catch ( InterruptedException e ) {
            LOGGER.error ( CLASS_NAME, "test", e );
        }
    }

}
