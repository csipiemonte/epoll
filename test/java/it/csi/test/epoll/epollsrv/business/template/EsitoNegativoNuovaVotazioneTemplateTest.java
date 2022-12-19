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
import it.csi.epoll.epollsrv.business.dto.PreferenzaDTO;
import it.csi.epoll.epollsrv.util.email.template.EsitoNegativoNuovaVotazioneTemplate;
import it.csi.epoll.epollsrv.util.exception.ManagedException;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;


/**
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class EsitoNegativoNuovaVotazioneTemplateTest {

    @Test
    public void testCreaContenutoEmail () {

        EsitoNegativoNuovaVotazioneTemplate nuova = new EsitoNegativoNuovaVotazioneTemplate ();
        nuova.setPollDTO ( creaDTO () );
        nuova.setMotivazione ( "motivazione di test" );
        String contenuto = nuova.creaContenutoEmail ();

        File emailInvitoVotazioneFile = new File("email_esito_negativo_nuova_votazione.html");
        
        try (FileOutputStream contenutoFile = new FileOutputStream(emailInvitoVotazioneFile)) {
			contenutoFile.write(contenuto.getBytes());
			contenutoFile.close();
			
			Desktop.getDesktop().browse(emailInvitoVotazioneFile.toURI());
		} catch (IOException e) {
			// NOOP
		}
        assertNotNull ( "Deve sempre ritornare un oggetto valorizzato", contenuto );
    }

    @Test
    public void verificaEccezioneSeModelloNullo () {
        try {
            EsitoNegativoNuovaVotazioneTemplate nuova = new EsitoNegativoNuovaVotazioneTemplate ();
            nuova.creaContenutoEmail ();

            fail ( "Il modello deve essere nullo" );
        } catch ( ManagedException e ) {
            assertTrue ( e.getMessage ().startsWith ( "Modello non creato" ) );
        }
    }

    private PollDTO creaDTO () {

        PollDTO poll = new PollDTO ();
        poll.setCodice ( "codice" );
        poll.setDtCreazione ( new Date ( System.currentTimeMillis () ) );
        poll.setDtFineValidita ( new Date ( System.currentTimeMillis () ) );
        poll.setDtInizioValidita ( new Date ( System.currentTimeMillis () ) );
        
        List<ElettoreDTO> elettori = new LinkedList<> ();
        ElettoreDTO elettore = new ElettoreDTO ();
        elettore.setCognomeNome ( "Prova Prova" );
        elettore.setEmail ( "prova@priva.it" );
        elettori.add ( elettore );
        
        poll.setElettori ( elettori );
        poll.setOggetto ( "oggetto" );
        
        List<PreferenzaDTO> preferenze = new ArrayList<PreferenzaDTO>();
        
        PreferenzaDTO p1 = new PreferenzaDTO();
        p1.setPreferenza( "Preferenza 1" );
		preferenze.add( p1 );
		
		PreferenzaDTO p2 = new PreferenzaDTO();
		p2.setPreferenza( "Preferenza 2" );
		preferenze.add( p2 );
		
		PreferenzaDTO p3 = new PreferenzaDTO();
		p3.setPreferenza( "Preferenza 3" );
		preferenze.add( p3 );
		
		poll.setPreferenze ( preferenze );
		poll.setTesto ( "Buongiorno, &egrave; stata indetta una nuova votazione.<br>La votazione prevede che venga espresso un voto in merito a XXXXXX." );
		
        return poll;
    }
}
