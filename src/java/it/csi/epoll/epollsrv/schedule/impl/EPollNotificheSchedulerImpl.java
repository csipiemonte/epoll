/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.schedule.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.RisultatiNotifichePollDTO;
import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.dto.email.MimeType;
import it.csi.epoll.epollsrv.business.service.BodyPreferenzaService;
import it.csi.epoll.epollsrv.business.service.EmailServiceSMTP;
import it.csi.epoll.epollsrv.business.service.PollService;
import it.csi.epoll.epollsrv.integration.domain.BodyPreferenza;
import it.csi.epoll.epollsrv.schedule.EpollNotificheScheduler;
import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.email.template.NotificaVotazioneTemplate;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 * @author fnaro Scheduler che si occupa di cercare e il numero dei votanti ed in numero di votanti previsti ed invia periodicamente una mail di aggiornamento
 */


@Service
@Transactional
public class EPollNotificheSchedulerImpl implements EpollNotificheScheduler {

    private static final long RATE_MILLIS = 120000L; // 2 minuti

    private static final String CLASS_NAME = "EPollNotificheSchedulerImpl";

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

    private void doExecute () throws IOException {
        final String methodName = "execute";

        LOGGER.info ( methodName, "Searching notification polls" );

        List<RisultatiNotifichePollDTO> risultatiPollList = pollService.cercaPerInvioAggiornamento ();

        if ( ( null != risultatiPollList ) && !risultatiPollList.isEmpty () ) {
            for ( RisultatiNotifichePollDTO risultatiPoll: risultatiPollList ) {

                LOGGER.info ( methodName, "Costruisco l'email da inviare" );
                NotificaVotazioneTemplate scrutinioVotazioneTemplate = new NotificaVotazioneTemplate ( risultatiPoll );
                String contenuto = scrutinioVotazioneTemplate.creaContenutoEmail ();

                List<BodyPreferenza> bodyPreferenzaList = bodyPreferenzaService.cercaPreferenzeByPollId ( risultatiPoll.getPollDTO ().getCodice () );

                EmailDTO email = new EmailDTO ( risultatiPoll.getPollDTO ().getEnte ().getId () );
                Map<String, String> headers = new LinkedHashMap<> ();
                headers.put ( Constants.HEADER_IDENTIFICATIVO_SISTEMA, "epoll" );

                email.setTo ( Arrays.asList ( risultatiPoll.getPollDTO ().getPresidente ().getEmail () ) );
                email.setBody ( contenuto );
                email.setOggetto ( String.format ( Constants.OGGETTO_NOTIFICA, risultatiPoll.getPollDTO ().getCodice () ) );
                email.setMimeType ( MimeType.TEXT_HTML );
                email.setHeaders ( headers );

                LOGGER.info ( methodName,
                    "Invio email notifica di aggiornamento della votazione con codice: " + risultatiPoll.getPollDTO ().getCodice () );

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
