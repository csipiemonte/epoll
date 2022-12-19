/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.service.EmailServiceSMTP;
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
public class EmailServiceSMTPImplTestLocal {

    @Autowired
    private EmailServiceSMTP eMailServiceSMTP;

    @Before
    public void init () {
        eMailServiceSMTP.connectMail ();
    }

    /**
     * Esemio di invio email: valorizzare almeno questi parametri per l'invio di una email
     */
    @Test
    @Ignore ( "Aggiunto ignore per evitare di intasare la casella di posta. USARE PER I TEST DI INTEGRAZIONE." )
    public void testSendMailOk () {
        Map<String, String> headers = new LinkedHashMap<> ();
        headers.put ( "idPoll", "DGBD34N34JNK345" );
        headers.put ( "idVotante", "EDERF234323" );
        headers.put ( "progressivo", "87543" );
        EmailDTO email = new EmailDTO ( Long.valueOf ( 1 ) ); // invio mail dalla casella dell'ente con id = 1
        email.setTo ( Arrays.asList ( "test2@cert.csi.it" ) ); // invio mail all'ente con ID= 2
        email.setBody ( "Body prova invio EMAIL da unit test: testSendMailOk" );
        email.setFrom ( "test1@cert.csi.it" );
        email.setHeaders ( headers );
        email.setOggetto ( "Oggetto prova invio EMAIL da unit test: testSendMailOk" );
        assertTrue ( "Il servizio di invio email deve rispondere true in caso di invio OK", eMailServiceSMTP.sendMail ( email ) );
    }
}
