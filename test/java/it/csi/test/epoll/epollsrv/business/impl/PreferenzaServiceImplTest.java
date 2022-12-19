/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.EnteService;
import it.csi.epoll.epollsrv.business.service.PollService;
import it.csi.epoll.epollsrv.business.service.PreferenzaService;
import it.csi.epoll.epollsrv.business.service.PresidenteService;
import it.csi.epoll.epollsrv.business.service.StatoPollService;
import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.Preferenza;
import it.csi.epoll.epollsrv.integration.domain.Presidente;
import it.csi.epoll.epollsrv.integration.repository.PreferenzaRepository;
import it.csi.test.epoll.epollsrv.business.IServiceTest;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;
import it.csi.test.epoll.epollsrv.testbed.helper.TestEntityHelper;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class PreferenzaServiceImplTest extends IServiceTest<PreferenzaService, PreferenzaRepository, Preferenza, Long> {

    @Autowired
    private PreferenzaService preferenzaService;

    @Autowired
    private PollService pollService;

    @Autowired
    private EnteService enteService;

    @Autowired
    private PresidenteService presidenteService;

    @Autowired
    private StatoPollService statoPollService;

    @Override
    protected PreferenzaService getService () {
        return preferenzaService;
    }

    @Override
    public Preferenza createEntity () {
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
        poll = pollService.save ( poll );
        Preferenza preferenza = TestEntityHelper.createPreferenza ();
        preferenza.setPoll ( poll );
        return preferenza;
    }

}
