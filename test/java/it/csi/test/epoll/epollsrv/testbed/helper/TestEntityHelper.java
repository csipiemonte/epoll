/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.testbed.helper;

import java.util.Date;

import it.csi.epoll.epollsrv.integration.domain.Elettore;
import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.integration.domain.PecCfg;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.Preferenza;
import it.csi.epoll.epollsrv.integration.domain.PreferenzaEspressa;
import it.csi.epoll.epollsrv.integration.domain.Presidente;
import it.csi.epoll.epollsrv.integration.domain.StatoPoll;


/**
 * Classe di utility da usare per creare gli oggetti comuni per gli unit test.
 */
public interface TestEntityHelper {

    static Ente createEnte () {
        return Ente.builder ()
            .withCodFiscale ( "CodiceFiscale" )
            .withCodice ( "codice" )
            .withDescrizione ( "descrizione" )
            .withPollList ( null )
            .withPresidenteList ( null )
            .build ();
    }

    static Elettore createElettore () {
        return Elettore.builder ()
            .withCognomeNome ( "Mario Rossi" )
            .withDtDataVotazione ( new Date () )
            .withEmail ( "marioRossi@mail.it" )
            .withPoll ( null )
            .build ();
    }

    static Poll createPoll () {
        return Poll.builder ()
            .withCodice ( "codice" )
            .withDtCreazione ( new Date () )
            .withDtFineValidita ( new Date () )
            .withDtInizioValidita ( new Date () )
            .withElettoreList ( null )
            .withEnte ( null )
            .withPreferenzaEspressaList ( null )
            .withPresidente ( null )
            .withOggetto ( "Oggetto mail" )
            .withTesto ( "Testo mail" )
            .withStatoPoll ( null )
            .build ();
    }

    static Preferenza createPreferenza () {
        return Preferenza.builder ()
            .withPoll ( null )
            .withPreferenza ( "preferenza" )
            .build ();
    }

    static PreferenzaEspressa createPreferenzaEspressa () {
        return PreferenzaEspressa.builder ()
            .withUuid ( "0012828588" )
            .withPoll ( null )
            .withPreferenza ( "preferenza" )
            .withConteggio ( 33 )
            .withValido ( Boolean.TRUE )
            .build ();
    }

    static Presidente createPresidente () {
        return Presidente.builder ()
            .withCognomeNome ( "Nicola Verdi" )
            .withEmail ( "nicolaVerdi@test.it" )
            .withEnte ( null )
            .withPollList ( null )
            .build ();
    }

    static PecCfg createPecCfg () {
        return PecCfg.builder ()
            .withImapInboxFolder ( "inbox" )
            .withImapServerHostname ( "Hostname" )
            .withImapUsername ( "testUsername" )
            .withImapPassword ( "testPass".getBytes () )
            .withIndirizzoPec ( "testPec@pec.it" )
            .withSmtpServerHostname ( "testHost" )
            .withSmtpUsername ( "testUser" )
            .withSmtpPassword ( "testPassword".getBytes () )
            .withSmtpServerPort ( 993 )
            .withImapServerPort ( 456 )
            .withId ( Long.valueOf ( 1 ) )
            .build ();
    }

    static StatoPoll createStatoPoll () {
        return StatoPoll.builder ()
            .withCodStato ( "C" )
            .withDescrizione ( "Creato" )
            .build ();
    }
}
