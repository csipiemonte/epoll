/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.util.email;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import it.csi.epoll.epollsrv.util.email.EmailParser;
import it.csi.epoll.epollsrv.util.exception.GenericParseErrorException;
import it.csi.epoll.epollsrv.util.exception.MissingRequiredSectionException;
import it.csi.epoll.epollsrv.util.exception.UnexpectedSectionException;
import junit.framework.Assert;

public class EmailParserTest {

    String emailBodyNoCRLF = 
        "Elettori:\r\n" +
        "Paolo Rossi (paolo.rossi@ente1.it)\r\n" +
        "Mario Bianchi (mario.bianchi@ente1.it)\r\n" +
        "Oggetto:\r\n" +
        "Votazione per decidere ...\r\n" +
        "Testo:\r\n" +
        "Siete chiamati a votare per decidere\r\n" +
        "se questo test funziona oppure no\r\n" +
        "Preferenze:\r\n" +
        "Blu\r\n" +
        "Giallo\r\n" +
        "Verde\r\n" +
        "Inizio: \r\n" +
        "30/05/2020 10:00\r\n" +
        "Fine:\r\n" +
        "15/05/2020 10:10\r\n";
    
    @Test
    public void parseNoCRLF() {
        
        try {
            Map<String, List<String>> res = EmailParser.parseEmailBody ( emailBodyNoCRLF, EmailParser.getDelimitersNewPoll () );
            
            System.out.println ( "\nTest parseNoCRLF" );
            for (String key : res.keySet ()) {
                System.out.println ( "<<" + key + ">>");
                for (String item : res.get ( key )) {
                    System.out.println ( item );
                }
            }
        } catch ( UnexpectedSectionException | MissingRequiredSectionException | GenericParseErrorException e ) {
            e.printStackTrace ();
            Assert.fail ( e.getMessage () );
        }
        
    }

    String emailBodySuccess = "Elettori:\r\n" +
        "Paolo Rossi (paolo.rossi@ente1.it)\r\n" +
        "Mario Bianchi (mario.bianchi@ente1.it)\r\n" +
        "\r\n" +
        "Oggetto:\r\n" +
        "Votazione per decidere ...\r\n" +
        "\r\n" +
        "Testo:\r\n" +
        "Siete chiamati a votare per decidere\r\n" +
        "se questo test funziona oppure no\r\n" +
        "\r\n" +
        "Preferenze:\r\n" +
        "Blu\r\n" +
        "Giallo\r\n" +
        "Verde\r\n" +
        "\r\n" +
        "Inizio: \r\n" +
        "30/05/2020 10:00\r\n" +
        "\r\n" +
        "Fine:\r\n" +
        "15/05/2020 10:10\r\n";

    @Test
    public void parseEmailBodySuccess() {
        
        try {
            Map<String, List<String>> res = EmailParser.parseEmailBody ( emailBodySuccess, EmailParser.getDelimitersNewPoll () );
            
            System.out.println ( "\nTest parseEmailBodySuccess" );
            for (String key : res.keySet ()) {
                System.out.println ( "<<" + key + ">>");
                for (String item : res.get ( key )) {
                    System.out.println ( item );
                }
            }
        } catch ( UnexpectedSectionException | MissingRequiredSectionException | GenericParseErrorException e ) {
            e.printStackTrace ();
            Assert.fail ( e.getMessage () );
        }
        
    }

    String emailBodyMultiPreferenzaSuccess = "Elettori:\r\n" +
        "Paolo Rossi (paolo.rossi@ente1.it)\r\n" +
        "Mario Bianchi (mario.bianchi@ente1.it)\r\n" +
        "\r\n" +
        "Oggetto:\r\n" +
        "Votazione per decidere ...\r\n" +
        "\r\n" +
        "Testo:\r\n" +
        "Siete chiamati a votare per decidere\r\n" +
        "se questo test funziona oppure no\r\n" +
        "\r\n" +
        "Numero massimo preferenze:\r\n" +
        "2\r\n" +
        "\r\n" +
        "Preferenze:\r\n" +
        "a  Blu\r\n" +
        "b  Giallo\r\n" +
        "c  Verde\r\n" +
        "d  Rosso\r\n" +
        "\r\n" +
        "Inizio: \r\n" +
        "30/05/2020 10:00\r\n" +
        "\r\n" +
        "Fine:\r\n" +
        "15/05/2020 10:10\r\n";

    @Test
    public void parseEmailBodyMultiPreferenzaSuccess() {
        
        try {
            Map<String, List<String>> res = EmailParser.parseEmailBody ( emailBodyMultiPreferenzaSuccess, EmailParser.getDelimitersNewPoll () );
            
            System.out.println ( "\nTest emailBodyMultiPreferenzaSuccess" );
            for (String key : res.keySet ()) {
                System.out.println ( "<<" + key + ">>");
                for (String item : res.get ( key )) {
                    System.out.println ( item );
                }
            }
        } catch ( UnexpectedSectionException | MissingRequiredSectionException | GenericParseErrorException e ) {
            e.printStackTrace ();
            Assert.fail ( e.getMessage () );
        }
        
    }

}
