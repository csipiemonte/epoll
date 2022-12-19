/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.EnteService;
import it.csi.epoll.epollsrv.business.service.PresidenteService;
import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.integration.domain.Presidente;
import it.csi.epoll.epollsrv.integration.repository.PresidenteRepository;
import it.csi.test.epoll.epollsrv.business.IServiceTest;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;
import it.csi.test.epoll.epollsrv.testbed.helper.TestEntityHelper;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class PresidenteServiceImplTest extends IServiceTest<PresidenteService, PresidenteRepository, Presidente, Long> {

    @Autowired
    private PresidenteService presidenteService;

    @Autowired
    private EnteService enteService;

    private String mailPresente = "utente1.test@ente1.it";

    private String mailAssente = "mail.non.presente@ente.non.esistente.it";

    @Override
    protected PresidenteService getService () {
        return presidenteService;
    }

    @Override
    public Presidente createEntity () {
        // creazione e salvataggio ente
        Ente ente = enteService.save ( TestEntityHelper.createEnte () );
        // creazione e salvataggio presidente
        Presidente presidente = TestEntityHelper.createPresidente ();
        presidente.setEnte ( ente );
        return presidente;
    }


    @Test
    public void cercaPresidenteConMailPresente () {
        Assert.assertEquals ( presidenteService.cercaPresidenteDaEmail ( mailPresente ).getEmail (), mailPresente );
    }

    @Test
    public void cercaPresidenteConMailNonPresente () {
        Assert.assertNull ( presidenteService.cercaPresidenteDaEmail ( mailAssente ) );
    }

}
