/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.EmailServiceIMAP;
import it.csi.epoll.epollsrv.business.service.EnteService;
import it.csi.epoll.epollsrv.util.ErrorMessages;
import it.csi.epoll.epollsrv.util.email.ricerca.SearchTermRead;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;

/**
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class EmailServiceIMAPImplTest {

    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_UTIL_LOG_CATEGORY, "EmailServiceImplTest" );

    @Autowired
    private EmailServiceIMAP eMailService;

    @Autowired
    private EnteService enteService;

    @Before
    public void init () {
        eMailService.connectMail ();
    }
    
    @Test
    public void testConnection () {
        assertTrue ( "Il servizio deve risultare connesso", eMailService.isConnected () );
        Boolean result = eMailService.isActive ();
        assertNotNull ( "Il servizio testStatus deve sempre ritornare un oggetto valorizzato", result );
        assertTrue ( "Il risultato del test deve essere sempre true", result );
    }

    @Test ( expected = IllegalArgumentException.class )
    public void testSearchError () {
        eMailService.searchMail ( new SearchTermRead ( Long.valueOf ( 1 ) ) );
    }

    @Test
    public void testDeleteMailNull () {
        assertTrue ( "Il risultato di una delete mail con mail nulla deve essere sempre true", eMailService.deleteMail ( null ) );
    }

    /**
     * L'operazione è valida. Viene settato un flag su un oggetto non appartenente alla casella di posta quindi
     */
    @Test
    public void testDeleteMailNoEnte () {
        Session session = null;
        eMailService.deleteMail ( new MimeMessage ( session ) );
    }

    @Test ( expected = IllegalArgumentException.class )
    public void testDisconnectMailError () {
        eMailService.disconnectMail ( null );
        assertFalse ( "Il servizio deve risultare NON connesso", eMailService.isConnected () );
    }

    @Test
    public void testLeggiEntiECaselleEmail () {
        //eMailService.searchMail ( new SearchTermRead ( idEnte ) ).forEach ( EmailDTO::toString )
        AtomicInteger numeroMail = new AtomicInteger ( 1 );
        enteService.findAll ().forEach ( ente -> eMailService.searchMail ( new SearchTermRead ( ente
            .getId () ) )
            .forEach ( email -> {
                assertNotNull ( ErrorMessages.G_PARAMETRO_ENTE_OBBLIGATORIO_NON_SPECIFICATO, email.getIdEnte () );
                LOGGER.info ( "testLeggiEntiECaselleEmail",
                        "MAIL NUMERO: " + numeroMail.getAndIncrement () + " ENTE: " + ente.getId ()
                            + "********************************************************" );
                LOGGER.info ( "testLeggiEntiECaselleEmail", email.toString () );
                } ) );
        enteService.findAll ().forEach ( ente -> eMailService.disconnectMail ( ente.getId () ) );
    }
}
