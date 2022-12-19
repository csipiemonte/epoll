/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.service.EmailServiceSMTP;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;


/**
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class EmailServiceSMTPImplTest {

    @Mock
    private EmailServiceSMTP eMailServiceSMTP;

    @Autowired
    private EmailServiceSMTP eMailServiceSMTPREAL;

    @Before
    public void init () {
        MockitoAnnotations.initMocks ( this );
        doThrow ( new IllegalArgumentException () ).when ( eMailServiceSMTP ).sendMail ( null );
        doReturn ( Boolean.TRUE ).when ( eMailServiceSMTP ).sendMail ( mock ( EmailDTO.class ) );
    }

    @Test
    public void testConnection () {
        eMailServiceSMTPREAL.connectMail ();
        assertTrue ( "Il servizio deve risultare connesso", eMailServiceSMTPREAL.isConnected () );
        Boolean result = eMailServiceSMTPREAL.isActive ();
        assertNotNull ( "Il servizio testStatus deve sempre ritornare un oggetto valorizzato", result );
        assertTrue ( "Il risultato del test deve essere sempre true", result );
    }

    @Test ( expected = IllegalArgumentException.class )
    public void testSendMailError () {
        eMailServiceSMTPREAL.sendMail ( null );
    }

    @Test ( expected = IllegalArgumentException.class )
    public void testSendMailError2 () {
        eMailServiceSMTPREAL.sendMail ( new EmailDTO ( null ) );
    }

    /**
     * Per eventuali ulteriori test di validazione prendere esempio.
     */
    @Test ( expected = IllegalArgumentException.class )
    public void testSendMailError3 () {
        eMailServiceSMTPREAL.sendMail ( new EmailDTO ( Long.valueOf ( 1 ) ) );
    }

    /**
     * Esemio di invio email: valorizzare almeno questi parametri per l'invio di una email
     */
    @Test
    @Ignore ( "Da sistemare" )
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
