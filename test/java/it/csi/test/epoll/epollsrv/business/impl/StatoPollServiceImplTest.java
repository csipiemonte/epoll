/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.StatoPollService;
import it.csi.epoll.epollsrv.integration.domain.StatoPoll;
import it.csi.epoll.epollsrv.integration.repository.StatoPollRepository;
import it.csi.test.epoll.epollsrv.business.IServiceTest;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;
import it.csi.test.epoll.epollsrv.testbed.helper.TestEntityHelper;


@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class StatoPollServiceImplTest extends IServiceTest<StatoPollService, StatoPollRepository, StatoPoll, String> {

    @Autowired
    private StatoPollService statoPollService;

    @Override
    protected StatoPollService getService () {
        return statoPollService;
    }

    @Override
    public StatoPoll createEntity () {

        return TestEntityHelper.createStatoPoll ();
    }

    @Override
    @Test ( expected = UnsupportedOperationException.class )
    public void testSave () {
        getService ().save ( createEntity () );
    }

    @Override
    @Test
    public void testFindOne () {
        StatoPoll result = getService ().findOne ( createEntity ().getId () );
        assertNotNull ( "Il servizio testSave deve sempre ritornare un oggetto valorizzato", result );
        assertNotNull ( "Il risultato del test deve restituire un ID valorizzato ", result.getId () );
    }

    @Override
    @Test ( expected = UnsupportedOperationException.class )
    public void testDelete () {
        getService ().delete ( createEntity ().getId () );
    }

    @SuppressWarnings ( "all" )
    @Override
    @Test
    public void testEquals () {
        StatoPoll toCompare = createEntity ();
        StatoPoll result = getService ().findOne ( toCompare.getId () );
        assertTrue ( "Le due entità devono essere uguali", toCompare.equals ( result ) );
        assertFalse ( "L'equals deve ritornare sempre false se confronto un oggetto nullo", toCompare.equals ( null ) );
        assertFalse ( "L'equals deve ritornare sempre false se confrontato con un oggetto di diverso tipo", result.equals ( this ) );
    }

    @Override
    @Test
    public void testHash () {
        StatoPoll toCompare = createEntity ();
        assertTrue ( "hashCode deve essere maggiore di 31", ( ( toCompare.hashCode () >= 31 ) || ( toCompare.hashCode () < 0 ) ) );
    }
}
