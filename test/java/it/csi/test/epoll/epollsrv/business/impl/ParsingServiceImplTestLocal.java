/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.dto.email.MimeType;
import it.csi.epoll.epollsrv.business.dto.email.ParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.dto.email.PreferenzaEspressaParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.impl.ParsingServiceImpl;
import it.csi.epoll.epollsrv.business.service.EmailServiceIMAP;
import it.csi.epoll.epollsrv.business.service.ParsingService;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.Preferenza;
import it.csi.epoll.epollsrv.integration.domain.Presidente;
import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.exception.GenericParameterException;
import it.csi.epoll.epollsrv.util.exception.GenericParseErrorException;
import it.csi.epoll.epollsrv.util.exception.IllegalMailSubjectException;
import it.csi.epoll.epollsrv.util.exception.ManagedException;
import it.csi.epoll.epollsrv.util.exception.MissingRequiredSectionException;
import it.csi.epoll.epollsrv.util.exception.NotEnoughPreferenzeException;
import it.csi.epoll.epollsrv.util.exception.NotValidDateException;
import it.csi.epoll.epollsrv.util.exception.UnexpectedSectionException;
import it.csi.test.epoll.epollsrv.testbed.config.EPollUnitTestInMemory;


/**
 * Classe di test per il servizio ParsingService. NON PIù UNIT TEST IN QUANTO INVIA EMAIL
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { EPollUnitTestInMemory.class } )
@Transactional
public class ParsingServiceImplTestLocal {

    @Autowired
    private EmailServiceIMAP emailServiceIMAP;

    private EmailDTO nullMessage;

    private Message noSenderMessage;

    private Message noSubjectMessage;

    private Message mittenteSconosciutoMessage;

    private Message pollEsistentePreferenzaValidaMessage;

    private Message pollEsistentePreferenzaNonEsistenteMessage;

    private Message pollEsistentePreferenzaTroppoPrestoMessage;

    private Message pollEsistentePreferenzaTroppoTardiMessage;

    private Message nuovoPollMessage;
    
    private Message nuovoPollMultiPreferenzaMessage;

    private Message nuovoPollNoCRLF;
    
    private Message multiPreferenzaPollMessagge;
    
    private Message multiPreferenzaPollMessaggeHtml;
    
    private Long idEnte;

    Properties defaultProps = new Properties ();

    private static final String POLL_1 = "POLL_1";

    @Spy
    @Autowired
    private ParsingService parsingService;

    @Before
    public void init () throws MessagingException {

        nullMessage = null;

        idEnte = Long.valueOf ( 1 );

        Session s = Session.getInstance ( defaultProps );
        String emailBodyFailure = "no content";

        noSenderMessage = new MimeMessage ( s );
        noSenderMessage.setFrom ( null );
        noSenderMessage.setContent ( emailBodyFailure, MimeType.TEXT_PLAIN.getValue () );

        InternetAddress mittentePresidente = new InternetAddress ( "test1@cert.csi.it" );
        InternetAddress mittenteSconosciuto = new InternetAddress ( "sconosciuto@ente1.it" );
        InternetAddress mittenteRiconosciuto = new InternetAddress ( "paolo.rossi@ente1.it" );

        String subjectPollEsistente = "Re: R: I: FWD: Nuova votazione POLL_1";
        String subjectPollNuovo = "Nuova votazione POLL_TEST";

        noSubjectMessage = new MimeMessage ( s );
        noSubjectMessage.setFrom ( mittentePresidente );
        noSubjectMessage.setSubject ( null );
        noSubjectMessage.setContent ( emailBodyFailure, MimeType.TEXT_PLAIN.getValue () );

        mittenteSconosciutoMessage = new MimeMessage ( s );
        mittenteSconosciutoMessage.setFrom ( mittenteSconosciuto );
        mittenteSconosciutoMessage.setSubject ( subjectPollNuovo );
        mittenteSconosciutoMessage.setContent ( emailBodyFailure, MimeType.TEXT_PLAIN.getValue () );

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

        String emailBodyNoCRLF = "Elettori:\r\n" +
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
            "Multi-preferenza:\r\n" +
            "SI\r\n" +
            "\r\n" +
            "Numero max preferenze:\r\n" +
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
        
        String emailMultiPrefHtml = "<p>&nbsp;</p>" + 
        		"<p>&nbsp;2</p>" + 
        		"<p>On 2021-03-25 10:24, Per conto di: test3@cert.csi.it wrote:</p>" + 
        		"<blockquote><!-- html ignored --> <!-- head ignored --><!-- meta ignored -->" + 
        		"<p>Siete chiamati a votare per decidere il presidente dell'assemblea</p>" + 
        		"<p>Per procedere alla votazione &egrave; necessario rispondere alla presente email digitando i codici delle preferenze in quantit&agrave; minore o uguale al numero indicato.</p>" + 
        		"<p>Si prega di porre la massima attenzione nella digitazione delle preferenze, nel caso in cui le preferenze non venissero espresse correttamente i voti non validi non saranno calcolati.</p>" + 
        		"<h3>Votazione multi-preferenza</h3>" + 
        		"<p>SI</p>\r\n" + 
        		"<h3>Numero massimo di preferenze da esprimere</h3>" + 
        		"<p>2</p>" + 
        		"<h3>Preferenze:</h3>" + 
        		"<p>1 Luigi Bianchi</p>" + 
        		"<p>2 Franco Paolo</p>" + 
        		"<p>3 Claudio Verdi</p>" + 
        		"<p>4 Paolo Paoli</p>" + 
        		"<br />" + 
        		"<p>Le preferenze devono essere espresse all'interno del periodo di votazione che inizia il giorno 25 marzo 2021 alle ore 11:00 e si conclude il giorno 26 marzo 2021 alle ore 12:00.</p>" + 
        		"Grazie.<br /><br /> Il presidente.</blockquote>";

        nuovoPollMessage = new MimeMessage ( s );
        nuovoPollMessage.setFrom ( mittentePresidente );
        nuovoPollMessage.setSubject ( subjectPollNuovo );
        nuovoPollMessage.setContent ( emailBodySuccess, MimeType.TEXT_PLAIN.getValue () );

		nuovoPollMultiPreferenzaMessage = new MimeMessage(s);
		nuovoPollMultiPreferenzaMessage.setFrom(mittentePresidente);
		nuovoPollMultiPreferenzaMessage.setSubject(subjectPollNuovo);
		nuovoPollMultiPreferenzaMessage.setContent(emailBodyMultiPreferenzaSuccess, MimeType.TEXT_PLAIN.getValue());
        
		nuovoPollNoCRLF = new MimeMessage(s);
		nuovoPollNoCRLF.setFrom(mittentePresidente);
		nuovoPollNoCRLF.setSubject(subjectPollNuovo);
		nuovoPollNoCRLF.setContent(emailBodyNoCRLF, MimeType.TEXT_PLAIN.getValue());
		
        multiPreferenzaPollMessagge = new MimeMessage(s);
        multiPreferenzaPollMessagge.setFrom(mittentePresidente);
        multiPreferenzaPollMessagge.setSubject(subjectPollNuovo);
        multiPreferenzaPollMessagge.setContent("Blu;Giallo,Pippo-Viola Rosso Lilla", MimeType.TEXT_PLAIN.getValue());
        
        multiPreferenzaPollMessaggeHtml = new MimeMessage(s);
        multiPreferenzaPollMessaggeHtml.setFrom(mittentePresidente);
        multiPreferenzaPollMessaggeHtml.setSubject(subjectPollNuovo);
        multiPreferenzaPollMessaggeHtml.setContent(emailMultiPrefHtml, MimeType.TEXT_HTML.getValue());
        

        pollEsistentePreferenzaValidaMessage = new MimeMessage ( s );
        pollEsistentePreferenzaValidaMessage.setFrom ( mittenteRiconosciuto );
        pollEsistentePreferenzaValidaMessage.setSubject ( subjectPollEsistente );
        pollEsistentePreferenzaValidaMessage.setHeader ( Constants.HEADER_REPLY_TO_POLL_INVITATION, POLL_1 );
        pollEsistentePreferenzaValidaMessage.setContent ( "Blu", MimeType.TEXT_PLAIN.getValue () );
        pollEsistentePreferenzaValidaMessage.setSentDate ( Calendar.getInstance ().getTime () );

        pollEsistentePreferenzaNonEsistenteMessage = new MimeMessage ( s );
        pollEsistentePreferenzaNonEsistenteMessage.setFrom ( mittenteRiconosciuto );
        pollEsistentePreferenzaNonEsistenteMessage.setSubject ( subjectPollEsistente );
        pollEsistentePreferenzaNonEsistenteMessage.setHeader ( Constants.HEADER_REPLY_TO_POLL_INVITATION, POLL_1 );
        pollEsistentePreferenzaNonEsistenteMessage.setContent ( "Preferenza non esistente", MimeType.TEXT_PLAIN.getValue () );
        pollEsistentePreferenzaNonEsistenteMessage.setSentDate ( Calendar.getInstance ().getTime () );

        pollEsistentePreferenzaTroppoPrestoMessage = new MimeMessage ( s );
        pollEsistentePreferenzaTroppoPrestoMessage.setFrom ( mittenteRiconosciuto );
        pollEsistentePreferenzaTroppoPrestoMessage.setSubject ( subjectPollEsistente );
        pollEsistentePreferenzaTroppoPrestoMessage.setHeader ( Constants.HEADER_REPLY_TO_POLL_INVITATION, POLL_1 );
        pollEsistentePreferenzaTroppoPrestoMessage.setContent ( "Blu", MimeType.TEXT_PLAIN.getValue () );
        Calendar troppoPresto = Calendar.getInstance ();
        troppoPresto.set ( Calendar.YEAR, 1800 );
        pollEsistentePreferenzaTroppoPrestoMessage.setSentDate ( troppoPresto.getTime () );

        pollEsistentePreferenzaTroppoTardiMessage = new MimeMessage ( s );
        pollEsistentePreferenzaTroppoTardiMessage.setFrom ( mittenteRiconosciuto );
        pollEsistentePreferenzaTroppoTardiMessage.setSubject ( subjectPollEsistente );
        pollEsistentePreferenzaTroppoTardiMessage.setHeader ( Constants.HEADER_REPLY_TO_POLL_INVITATION, POLL_1 );
        pollEsistentePreferenzaTroppoTardiMessage.setContent ( "Blu", MimeType.TEXT_PLAIN.getValue () );
        Calendar tropoTardi = Calendar.getInstance ();
        tropoTardi.set ( Calendar.YEAR, 9999 );
        pollEsistentePreferenzaTroppoTardiMessage.setSentDate ( tropoTardi.getTime () );
    }
    
    @Test
    public void isActive () {
        Assert.assertTrue ( parsingService.isActive () );
    }

    @Test ( expected = Exception.class )
    public void parseIncomingNullMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        parsingService.parseIncomingMessage ( nullMessage );
    }

    @Test ( expected = NullPointerException.class )
    public void parseIncomingNoSenderMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( noSenderMessage, idEnte ) );
    }

    @Test ( expected = IllegalArgumentException.class )
    public void parseIncomingNoSubjectMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( noSubjectMessage, idEnte ) );
    }

    @Test ( expected = ManagedException.class )
    public void parseIncomingMittenteSconosciutoMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( mittenteSconosciutoMessage, idEnte ) );
    }

    @Test
    //    @Ignore ( "DA SISTEMARE PER NON MANDARE EMAIL" )
    public void parseIncomingNuovoPollNoCRLF ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        ParsedIncomingMessage parsedMessage = parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( nuovoPollNoCRLF, idEnte ) );
        Assert.assertNotNull ( parsedMessage.getCodicePoll () );
    }

    @Test
    //  @Ignore ( "DA SISTEMARE PER NON MANDARE EMAIL" )
    public void parseIncomingNuovoPollMultiPreferenzaMessage() throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
    	ParsedIncomingMessage parsedMessage = parsingService.parseIncomingMessage( emailServiceIMAP.parseEmailToDTO(nuovoPollMultiPreferenzaMessage, idEnte) );
    	Assert.assertNotNull(parsedMessage.getCodicePoll());
    }

    @Test
    //  @Ignore ( "DA SISTEMARE PER NON MANDARE EMAIL" )
    public void parseIncomingNuovoPollMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        ParsedIncomingMessage parsedMessage = parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( nuovoPollMessage, idEnte ) );
        Assert.assertNotNull ( parsedMessage.getCodicePoll () );
    }
    
    @Test
    @Ignore ( "DA SISTEMARE PER NON MANDARE EMAIL" )
    public void parseIncomingPollEsistentePreferenzaValidaMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        ParsedIncomingMessage parsedMessage
            = parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( pollEsistentePreferenzaValidaMessage, idEnte ) );
        Assert.assertTrue ( ( (PreferenzaEspressaParsedIncomingMessage) parsedMessage ).getPreferenzaEspressa ().getValido () );
    }

    @Test
    @Ignore ( "DA SISTEMARE PER NON MANDARE EMAIL" )
    public void parseIncomingPollEsistentePreferenzaNonEsistenteMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        ParsedIncomingMessage parsedMessage
            = parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( pollEsistentePreferenzaNonEsistenteMessage, idEnte ) );
        Assert.assertFalse ( ( (PreferenzaEspressaParsedIncomingMessage) parsedMessage ).getPreferenzaEspressa ().getValido () );
    }

    @Test
    @Ignore ( "DA SISTEMARE PER NON MANDARE EMAIL" )
    public void parseIncomingPollEsistentePreferenzaTroppoPrestoMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        ParsedIncomingMessage parsedMessage
            = parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( pollEsistentePreferenzaTroppoPrestoMessage, idEnte ) );
        Assert.assertFalse ( ( (PreferenzaEspressaParsedIncomingMessage) parsedMessage ).getPreferenzaEspressa ().getValido () );
    }

    @Test
    @Ignore ( "DA SISTEMARE PER NON MANDARE EMAIL" )
    public void parseIncomingPollEsistentePreferenzaTroppoTardiMessage ()
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    MessagingException, NotValidDateException, NotEnoughPreferenzeException, GenericParameterException {
        ParsedIncomingMessage parsedMessage
            = parsingService.parseIncomingMessage ( emailServiceIMAP.parseEmailToDTO ( pollEsistentePreferenzaTroppoTardiMessage, idEnte ) );
        Assert.assertFalse ( ( (PreferenzaEspressaParsedIncomingMessage) parsedMessage ).getPreferenzaEspressa ().getValido () );
    }
    
    @Test
    public void parsePreferenzaEspressa() throws MessagingException, IOException {
    	EmailDTO emailDTO = emailServiceIMAP.parseEmailToDTO ( nuovoPollMessage, idEnte );
    	emailDTO.setFrom("mario.draghi@csi.it");
		Poll existingPoll = new Poll();
		Presidente presidente = new Presidente();
		presidente.setEmail(emailDTO.getFrom());
		existingPoll.setPresidente(presidente );
		existingPoll.setId(1l);
		parsingService.parsePreferenzaEspressa(existingPoll, emailDTO);
    }
    
    @Test
    public void parsePreferenzeEspresse() throws MessagingException, IOException{
    	EmailDTO emailDTO = emailServiceIMAP.parseEmailToDTO ( multiPreferenzaPollMessaggeHtml, idEnte );
    	emailDTO.setFrom("mario.draghi@csi.it");
		Poll existingPoll = new Poll();
		Presidente presidente = new Presidente();
		presidente.setEmail(emailDTO.getFrom());
		existingPoll.setPresidente(presidente );
		existingPoll.setId(1l);
		List<Preferenza> preferenzePoll = new LinkedList<>();
		Preferenza p1 = Preferenza.builder().withId(1l).withPreferenza("Luigi Bianchi").withPoll(existingPoll).withCodPreferenza("1").build();
		Preferenza p2 = Preferenza.builder().withId(2l).withPreferenza("Franco Paolo").withPoll(existingPoll).withCodPreferenza("2").build();
		Preferenza p3 = Preferenza.builder().withId(3l).withPreferenza("Claudio Verdi").withPoll(existingPoll).withCodPreferenza("3").build();
		Preferenza p4 = Preferenza.builder().withId(4l).withPreferenza("Paolo Paoli").withPoll(existingPoll).withCodPreferenza("4").build();
		preferenzePoll.add(p1);
		preferenzePoll.add(p2);
		preferenzePoll.add(p3);
		preferenzePoll.add(p4);
		existingPoll.setPreferenzaList(preferenzePoll);
		parsingService.parsePreferenzeEspresse(existingPoll, emailDTO);
    }

}
