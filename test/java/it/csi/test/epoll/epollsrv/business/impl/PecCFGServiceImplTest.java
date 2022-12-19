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
import it.csi.epoll.epollsrv.business.service.PecCfgService;
import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.integration.domain.PecCfg;
import it.csi.epoll.epollsrv.integration.repository.PecCfgRepository;
import it.csi.test.epoll.epollsrv.business.IServiceTest;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;
import it.csi.test.epoll.epollsrv.testbed.helper.TestEntityHelper;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class PecCFGServiceImplTest extends IServiceTest<PecCfgService, PecCfgRepository, PecCfg, Long> {

    @Autowired
    private PecCfgService pecCfgService;

    @Autowired
    private EnteService enteService;

    @Override
    protected PecCfgService getService () {
        return pecCfgService;
    }

    @Override
    public PecCfg createEntity () {
        // creazione e salvataggio ente
        Ente ente = enteService.save ( TestEntityHelper.createEnte () );
        PecCfg pecCfgTest = TestEntityHelper.createPecCfg ();
        pecCfgTest.setEnte ( ente );
        return pecCfgTest;
    }

}
