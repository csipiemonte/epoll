/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.template;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.ElettoreDTO;
import it.csi.epoll.epollsrv.business.dto.PollDTO;
import it.csi.epoll.epollsrv.business.dto.PreferenzaEspressaDTO;
import it.csi.epoll.epollsrv.business.dto.RisultatiScrutinioPollDTO;
import it.csi.epoll.epollsrv.util.email.template.ScrutinioVotazioneTemplate;
import it.csi.epoll.epollsrv.util.exception.ManagedException;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;


/**
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class ScrutinioVotazioneTemplateTest {

    @Test
    public void testCreaContenutoEmail () {

        ScrutinioVotazioneTemplate nuova = new ScrutinioVotazioneTemplate ();
        nuova.setRisultatiScrutinio ( creaDTO () );
        String contenuto = nuova.creaContenutoEmail ();

        File emailInvitoVotazioneFile = new File ( "email_scrutinio_votazione.html" );

        try ( FileOutputStream contenutoFile = new FileOutputStream ( emailInvitoVotazioneFile ) ) {
            contenutoFile.write ( contenuto.getBytes () );

            Desktop.getDesktop ().browse ( emailInvitoVotazioneFile.toURI () );
        } catch ( IOException e ) {
            // NOOP
        }
        assertNotNull ( "Deve sempre ritornare un oggetto valorizzato", contenuto );
    }

    @Test
    public void verificaEccezioneSeModelloNullo () {
        try {
            ScrutinioVotazioneTemplate nuova = new ScrutinioVotazioneTemplate ();
            nuova.creaContenutoEmail ();

            fail ( "Il modello deve essere nullo" );
        } catch ( ManagedException e ) {
            assertTrue ( e.getMessage ().startsWith ( "Modello non creato" ) );
        }
    }

    private RisultatiScrutinioPollDTO creaDTO () {

        PollDTO poll = new PollDTO ();
        poll.setCodice ( "codice" );
        poll.setDtCreazione ( new Date ( System.currentTimeMillis () ) );
        poll.setDtFineValidita ( new Date ( System.currentTimeMillis () ) );
        poll.setDtInizioValidita ( new Date ( System.currentTimeMillis () ) );

        List<ElettoreDTO> elettori = new LinkedList<> ();
        ElettoreDTO elettore = new ElettoreDTO ();
        elettore.setCognomeNome ( "Prova Prova" );
        elettore.setEmail ( "prova@priva.it" );
        elettore.setDtDataVotazione ( new Date ( System.currentTimeMillis () ) );
        elettori.add ( elettore );

        poll.setElettori ( elettori );
        poll.setOggetto ( "oggetto" );

        List<PreferenzaEspressaDTO> preferenzeEspresse = new ArrayList<> ();

        PreferenzaEspressaDTO p1 = new PreferenzaEspressaDTO ();
        p1.setPreferenza ( "Preferenza 1" );
        p1.setConteggio ( 5 );
        p1.setValido ( true );
        preferenzeEspresse.add ( p1 );

        PreferenzaEspressaDTO p2 = new PreferenzaEspressaDTO ();
        p2.setPreferenza ( "Preferenza 2" );
        p2.setConteggio ( 1 );
        p2.setValido ( true );
        preferenzeEspresse.add ( p2 );

        PreferenzaEspressaDTO p3 = new PreferenzaEspressaDTO ();
        p3.setPreferenza ( "Preferenza 3" );
        p3.setConteggio ( 2 );
        p3.setValido ( false );
        preferenzeEspresse.add ( p3 );

        PreferenzaEspressaDTO p4 = new PreferenzaEspressaDTO ();
        p4.setPreferenza ( "Preferenza 4" );
        p4.setConteggio ( 1 );
        p4.setValido ( true );
        preferenzeEspresse.add ( p4 );

        poll.setPreferenzeEspresse ( preferenzeEspresse );

        return new RisultatiScrutinioPollDTO ( poll );
    }
}
