/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.schedule;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.EmailServiceIMAP;
import it.csi.epoll.epollsrv.business.service.EnteService;
import it.csi.epoll.epollsrv.schedule.EPollScrutinioScheduler;
import it.csi.epoll.epollsrv.util.email.ricerca.SearchTermRead;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;


/**
 * @author abora
 * 
 * 
 *         TRATTASI DI INTEGRATION UNIT TEST. Usare con cautela. Ricordarsi di pulire la casella email dopo ogni test e commentare @Ignore
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class EPollScrutinoSchedulerImplTestLocal {

    private static final EpollsrvLogger LOGGER
        = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_UTIL_LOG_CATEGORY, "EPollScrutinoSchedulerImplTestLocal" );

    @Autowired
    private EPollScrutinioScheduler ePollScrutinioScheduler;

    @Autowired
    private EnteService enteService;

    @Autowired
    private EmailServiceIMAP eMailService;

    boolean emailInviata;

    @Before
    public void init () {
        eMailService.connectMail ();
        emailInviata = false;
    }

    /**
     * Invio dell'email per i risultati dello scrutinio
     */
    @Test
    @Ignore ( "Aggiunto ignore per evitare di intasare la casella di posta. USARE PER I TEST DI INTEGRAZIONE." )
    public void testSendMailRisultatiScrutinio () {

        // Invio la mail con i risultati dello scrutinio
        ePollScrutinioScheduler.execute ();

        // Verifico che l'email sia stata davvero inviata
        enteService.findAll ().forEach ( ente -> {
            SearchTermRead searchTerm = new SearchTermRead ( ente.getId () );
            eMailService.searchMail ( searchTerm )
                .forEach ( email -> {
                    if ( /*
                              * email.getHeaders ().containsKey ( Constants.HEADER_IDENTIFICATIVO_SISTEMA ) &&
                              */ email.getOggetto ().contains ( "Risultati scrutinio della votazione con codice:" ) ) {
                        emailInviata = true;
                    }

                    LOGGER.info ( "testSendMailRisultatiScrutinio", email.toString () );
                } );
        } );
        enteService.findAll ().forEach ( ente -> eMailService.disconnectMail ( ente.getId () ) );

        assertTrue ( "Email non inviata", emailInviata );

    }

}
