/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.RisultatiScrutinioPollDTO;
import it.csi.epoll.epollsrv.business.dto.StatoPollEnum;
import it.csi.epoll.epollsrv.business.service.EnteService;
import it.csi.epoll.epollsrv.business.service.PollService;
import it.csi.epoll.epollsrv.business.service.PresidenteService;
import it.csi.epoll.epollsrv.business.service.StatoPollService;
import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.Presidente;
import it.csi.epoll.epollsrv.integration.repository.PollRepository;
import it.csi.test.epoll.epollsrv.business.IServiceTest;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;
import it.csi.test.epoll.epollsrv.testbed.helper.TestEntityHelper;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class PollServiceImplTest extends IServiceTest<PollService, PollRepository, Poll, Long> {

    private String codicePollPresente = "POLL_1";

    private String codicePollAssente = "POLL_ASSENTE";

    private Poll pollVinceGiallo;

    private Poll pollExAequo;

    private Poll pollNoPreferenzeValide;

    @Autowired
    private PollService pollService;

    @Autowired
    private EnteService enteService;

    @Autowired
    private PresidenteService presidenteService;

    @Autowired
    private StatoPollService statoPollService;

    @Override
    protected PollService getService () {
        return pollService;
    }

    @Override
    public Poll createEntity () {
        // creazione e salvataggio ente
        Ente ente = enteService.save ( TestEntityHelper.createEnte () );
        // creazione e salvataggio presidente
        Presidente presidente = TestEntityHelper.createPresidente ();
        presidente.setEnte ( ente );
        presidente = presidenteService.save ( presidente );
        // creazione e salvataggio poll
        Poll poll = TestEntityHelper.createPoll ();
        poll.setEnte ( ente );
        poll.setPresidente ( presidente );
        poll.setStatoPoll ( statoPollService.findOne ( TestEntityHelper.createStatoPoll ().getId () ) );
        return poll;
    }

    @Before
    public void init () {
        pollVinceGiallo = pollService.cercaPollPerCodice ( "POLL_VINCE_GIALLO" );
        pollExAequo = pollService.cercaPollPerCodice ( "POLL_EX_AEQUO_GIALLO_VERDE" );
        pollNoPreferenzeValide = pollService.cercaPollPerCodice ( "POLL_NO_PREF_VALIDE" );
    }

    @Test
    public void cercaPollPresente () {
        assertEquals ( pollService.cercaPollPerCodice ( codicePollPresente ).getCodice (),
            codicePollPresente );
    }

    @Test
    public void cercaPollAssente () {
        assertNull ( pollService.cercaPollPerCodice ( codicePollAssente ) );
    }

    @Test
    public void cercaPollDaScrutinare () {
        List<Poll> pollDaScrutinare = pollService.cercaPollDaScrutinare ();
        assertNotNull ( pollDaScrutinare );
        assertEquals ( pollDaScrutinare.size (), 4 );
    }

    @Test
    public void scrutinaPollSuccessVotoGialloVincitore () {
        RisultatiScrutinioPollDTO risultati = pollService.scrutinaPoll ( pollVinceGiallo );
        assertEquals ( risultati.getPreferenzeValide ().get ( 0 ).getPreferenza (), "Giallo" );
    }

    @Test
    public void scrutinaPollSuccessExAequo () {
        RisultatiScrutinioPollDTO risultati = pollService.scrutinaPoll ( pollExAequo );
        assertEquals ( risultati.getPreferenzeValide ().get ( 0 ).getConteggio (), risultati.getPreferenzeValide ().get ( 1 ).getConteggio () );
    }

    @Test
    public void scrutinaPollSuccessNoPreferenzeValide () {
        RisultatiScrutinioPollDTO risultati = pollService.scrutinaPoll ( pollNoPreferenzeValide );
        assertEquals ( risultati.getPreferenzeValide ().size (), 0 );
        assertTrue ( "non tutte le preferenze sono non valide", risultati.getPreferenzeValide ().isEmpty () );
    }

    @Test
    public void cercaEScrutinaPoll () {
        List<RisultatiScrutinioPollDTO> risultatiList = pollService.cercaEScrutinaPoll ();
        assertNotNull ( risultatiList );
        for ( RisultatiScrutinioPollDTO risultati: risultatiList ) {
            assertTrue ( StatoPollEnum.SCRUTINATO.equals ( risultati.getPollDTO ().getStatoPoll () ) );
        }
    }

}
