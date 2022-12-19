/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import it.csi.epoll.epollsrv.business.dto.RisultatiNotifichePollDTO;
import it.csi.epoll.epollsrv.business.dto.RisultatiScrutinioPollDTO;
import it.csi.epoll.epollsrv.business.dto.StatoPollEnum;
import it.csi.epoll.epollsrv.business.service.PollService;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.StatoPoll;
import it.csi.epoll.epollsrv.integration.repository.PollRepository;
import it.csi.epoll.epollsrv.integration.repository.StatoPollRepository;
import it.csi.epoll.epollsrv.util.email.template.PollConverter;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 *
 */
@Service
@Transactional
public class PollServiceImpl implements PollService {

    private static final String CLASS_NAME = "ParsingServiceImpl";

    private static final Long FREQUENZA_MINIMA_DI_AGGIORNAMENTO = 2L;

    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_BUSINESS_LOG_CATEGORY, CLASS_NAME );

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private StatoPollRepository statoPollRepository;

    @Override
    public PollRepository getRepository () {
        return pollRepository;
    }

    @Override
    @Transactional ( readOnly = true )
    public Poll cercaPollPerCodice ( String codicePoll ) {
        return pollRepository.findByCodice ( codicePoll );
    }

    @Override
    @Transactional ( rollbackFor = Exception.class )
    public List<RisultatiScrutinioPollDTO> cercaEScrutinaPoll () {

        String method = "cercaEScrutinaPoll";

        LOGGER.begin ( CLASS_NAME, method );

        List<RisultatiScrutinioPollDTO> risultati = new LinkedList<> ();

        StatoPoll statoPoll = statoPollRepository.findOne ( StatoPollEnum.CREATO.getCodStato () );

        Assert.notNull ( statoPoll, "Stato poll 'Creato (C)' non presente sulla EPOLL_D_STATO_POLL" );

        List<Poll> pollDaScrutinare = cercaPollDaScrutinare ();

        if ( ( null != pollDaScrutinare ) && !pollDaScrutinare.isEmpty () ) {

            LOGGER.info ( method, String.format ( "Trovati %d poll da scrutinare", pollDaScrutinare.size () ) );

            for ( Poll poll: pollDaScrutinare ) {

                LOGGER.info ( method, String.format ( "Scrutinio del poll %s", poll.getOggetto () ) );

                risultati.add ( scrutinaPoll ( poll ) );
            }

        } else {

            LOGGER.info ( method, "Nessun poll da scrutinare" );

        }

        LOGGER.end ( CLASS_NAME, method );

        return risultati;
    }

    @Override
    @Transactional ( readOnly = true )
    public List<Poll> cercaPollDaScrutinare () {

        String method = "cercaPollDaScrutinare";

        LOGGER.begin ( CLASS_NAME, method );

        StatoPoll statoPoll = statoPollRepository.findOne ( StatoPollEnum.CREATO.getCodStato () );

        Assert.notNull ( statoPoll, "Stato poll 'Creato (C)' non presente sulla EPOLL_D_STATO_POLL" );

        LOGGER.end ( CLASS_NAME, method );

        Calendar now = Calendar.getInstance ();
        
        // Aggiunto offset di 2 minuti per evitare di non considerare i voti pervenuti nell'ultimo minuto di validità del poll
        now.add ( Calendar.MINUTE, -2 );
        
        return pollRepository.findAllByStatoPollAndDtFineValiditaBefore ( statoPoll, now.getTime () );
    }

    @Override
    public RisultatiScrutinioPollDTO scrutinaPoll ( Poll poll ) {

        String method = "scrutinaPoll";

        LOGGER.begin ( CLASS_NAME, method );

        Assert.notNull ( poll, "Poll da scrutinare null" );

        Assert.hasText ( poll.getCodice (), "Poll da scrutinare senza codice" );

        poll = pollRepository.findByCodice ( poll.getCodice () );

        Assert.notNull ( poll, "Poll da scrutinare non presente su db" );

        StatoPoll statoPollScrutinato = statoPollRepository.findOne ( StatoPollEnum.SCRUTINATO.getCodStato () );

        Assert.notNull ( statoPollScrutinato, "Stato poll 'Scrutinato (S)' non presente sulla EPOLL_D_STATO_POLL" );

        poll.setStatoPoll ( statoPollScrutinato );

        RisultatiScrutinioPollDTO risultati = new RisultatiScrutinioPollDTO ( PollConverter.pollEntityToDTO ( poll ) );

        pollRepository.save ( poll );

        LOGGER.end ( CLASS_NAME, method );

        return risultati;
    }

    @Override
    @Transactional ( rollbackFor = Exception.class )
    public List<RisultatiNotifichePollDTO> cercaPerInvioAggiornamento () {
        String method = "cercaPollDaScrutinare";

        LOGGER.begin ( CLASS_NAME, method );

        StatoPoll statoPoll = statoPollRepository.findOne ( StatoPollEnum.CREATO.getCodStato () );

        Assert.notNull ( statoPoll, "Stato poll 'Creato (C)' non presente sulla EPOLL_D_STATO_POLL" );

        List<RisultatiNotifichePollDTO> risultati = new LinkedList<> ();
        List<Poll> pollDaNotificare
            = pollRepository.findAllByStatoPollAndFrequenzaAggiornamentoGreaterThanEqual ( statoPoll, FREQUENZA_MINIMA_DI_AGGIORNAMENTO );

        if ( ( null != pollDaNotificare ) && !pollDaNotificare.isEmpty () ) {
            LOGGER.info ( method, String.format ( "Trovati %d  possibili poll da notificare, verifico le date", pollDaNotificare.size () ) );
            for ( Poll poll: pollDaNotificare ) {

                Calendar cal = Calendar.getInstance ();
                cal.setTime ( poll.getDtUltimaNotifica () );
                cal.add ( Calendar.MINUTE, poll.getFrequenzaAggiornamento ().intValue () );

                if ( new Date ().after ( cal.getTime () ) ) {
                    LOGGER.info ( method, String.format ( "Trovati %d poll da notificare", pollDaNotificare.size () ) );
                    LOGGER.info ( method, String.format ( "Notifica del poll %s", poll.getOggetto () ) );
                    risultati.add ( notificaPoll ( poll ) );
                } else {

                    LOGGER.info ( method, "Nessun poll da notificare" );
                }

            }

        } else {

            LOGGER.info ( method, "Nessun poll da notificare" );

        }

        LOGGER.end ( CLASS_NAME, method );


        return risultati;
    }

    @Override
    public RisultatiNotifichePollDTO notificaPoll ( Poll poll ) {

        String method = "notificaPoll";

        LOGGER.begin ( CLASS_NAME, method );

        Assert.notNull ( poll, "Poll da notificare null" );

        Assert.hasText ( poll.getCodice (), "Poll da notificare senza codice" );

        poll = pollRepository.findByCodice ( poll.getCodice () );

        Assert.notNull ( poll, "Poll da notificare non presente su db" );


        poll.setDtUltimaNotifica ( new Date () );

        RisultatiNotifichePollDTO risultati = new RisultatiNotifichePollDTO ( PollConverter.pollEntityToDTO ( poll ) );

        pollRepository.save ( poll );

        LOGGER.end ( CLASS_NAME, method );

        return risultati;
    }

}
