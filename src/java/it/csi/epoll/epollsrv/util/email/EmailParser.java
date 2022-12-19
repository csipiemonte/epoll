/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.util.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.util.StringUtils;

import it.csi.epoll.epollsrv.business.dto.ParsingPollOutputDTO;
import it.csi.epoll.epollsrv.util.exception.GenericParseErrorException;
import it.csi.epoll.epollsrv.util.exception.IllegalMailRecipientFormat;
import it.csi.epoll.epollsrv.util.exception.IllegalMailSubjectException;
import it.csi.epoll.epollsrv.util.exception.MissingRequiredSectionException;
import it.csi.epoll.epollsrv.util.exception.UnexpectedSectionException;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


public class EmailParser {

    private static final String CLASS_NAME = "EmailParser";

    private static final String METHOD_NAME_PARSE_EMAIL_BODY = "parseEmailBody";

    private static final String METHOD_NAME_PARSE_EMAIL_HTML_BODY = "parseEmailHTMLBody";

    private static final String METHOD_NAME_PARSE_EMAIL_RECIPIENT = "parseRecipient";

    private static final String METHOD_NAME_PARSE_POLL_CODE_FROM_SUBJECT = "parsePollCodeFromSubject";

    private static final EpollsrvLogger logger = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_UTIL_LOG_CATEGORY,
        CLASS_NAME );

    private static Pattern recipentPattern = Pattern.compile ( "(^[\\w ]+)[(]([\\w\\.\\_]+\\@[\\w\\.]+)[)]" );

    private static String subjectStartString = "nuova votazione";
    
    private static String subjectClosingString = "chiusura votazione";

    private static String subjectPreferenceString = "preferenza";

    public static final String EMAIL_SECTION_DELIMITER_ELETTORI = "Elettori";

    public static final String EMAIL_SECTION_DELIMITER_OGGETTO = "Oggetto";

    public static final String EMAIL_SECTION_DELIMITER_TESTO = "Testo";

    public static final String EMAIL_SECTION_DELIMITER_PREFERENZE = "Preferenze";
    
    public static final String EMAIL_SECTION_DELIMITER_NUMERO_MAX_PREFERENZE = "Numero massimo preferenze";
    
    public static final String EMAIL_SECTION_DELIMITER_INIZIO = "Inizio";

    public static final String EMAIL_SECTION_DELIMITER_FINE = "Fine";

    public static final String EMAIL_SECTION_DELIMITER_NOTIFICHE = "Notifiche";

    private static final Set<EmailParserSectionDelimiter> sectionDelimitersNewPoll = new HashSet<> ();

    static {

        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_ELETTORI, true ) );
        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_OGGETTO, true ) );
        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_TESTO, true ) );
        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_NUMERO_MAX_PREFERENZE, false ) );
        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_PREFERENZE, true ) );
        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_INIZIO, false ) );
        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_FINE, true ) );
        sectionDelimitersNewPoll.add ( new EmailParserSectionDelimiter ( EmailParser.EMAIL_SECTION_DELIMITER_NOTIFICHE, false ) );
    }

    private EmailParser () {
    }

    /*
     * Template delle mail Elettori: Paolo Rossi (paolo.rossi@ente.it) Mario Bianchi (mario.bianchi@ente.it) Oggetto: Votazione per decidere ... Testo: Siete
     * chiamati a votare per ..... Preferenze: Blu Giallo Verde Inizio: dd/mm/yyyy hh:mm Fine: dd/mm/yyyy hh:mm
     */
    public static Map<String, List<String>> parseEmailBody ( String emailBody, Set<EmailParserSectionDelimiter> sectionDelimiters )
                    throws UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException {

        logger.begin ( CLASS_NAME, METHOD_NAME_PARSE_EMAIL_BODY );

        Map<String, List<String>> sectionsText = new HashMap<> ();

        try ( BufferedReader reader = new BufferedReader ( new StringReader ( emailBody ) ) ) {

            boolean done = false;
            boolean inSection = false;

            ArrayList<String> sectionRows = null;

            // loop principale
            while ( !done ) {
                // si legge una riga dal corpo della mail
                String line = reader.readLine ();

                // se non e' null procedo
                if ( line != null ) {
                    String trimmedLine = line.trim ();

                    if ( trimmedLine.length () > 0 ) {

                        EmailParserSectionDelimiter delimiter = null;
                        if ( trimmedLine.endsWith ( ":" ) ) {
                            // verifico se la linea rientra tra i delimitatori passati in ingresso
                            delimiter = sectionDelimiters.stream ()
                                .filter ( del -> trimmedLine.equals ( del.getSetionDelimiter ().concat ( ":" ) ) )
                                .findAny ()
                                .orElse ( null );
                            if (delimiter == null) {
                                // la linea non rientra tra i delimitatori e siamo fuori sezione...eccezione
                                throw new UnexpectedSectionException ( trimmedLine );
                            }
                        }

                        if ( delimiter != null ) {
                            sectionRows = new ArrayList<> ();
                            sectionsText.put ( delimiter.getSetionDelimiter (), sectionRows );

                            inSection = true;
                        } else {
                            if (inSection) {
                                sectionRows.add ( trimmedLine );
                            } else {
                                // la linea non rientra tra i delimitatori e siamo fuori sezione...eccezione
                                throw new UnexpectedSectionException ( trimmedLine );
                            }
                        }
                    }
                    
                    /*
                    // se siamo all'interno di una sezione collezioniamo le righe di testo
                    if ( inSection ) {
                        // riga vuota, fine sezione
                        if ( trimmedLine.length () == 0 ) {
                            inSection = false;

                        } else {
                            // aggiungo al buffer di output
                            sectionRows.add ( trimmedLine );
                        }
                    } else {
                        if ( trimmedLine.length () > 0 ) {
                            // verifico se la linea rientra tra i delimitatori passati in ingresso
                            EmailParserSectionDelimiter delimiter = sectionDelimiters.stream ()
                                .filter ( del -> trimmedLine.equals ( del.getSetionDelimiter ().concat ( ":" ) ) )
                                .findAny ()
                                .orElse ( null );

                            if ( delimiter != null ) {
                                // inizializzo il buffer e lo colleziono nella map di output
                                sectionRows = new ArrayList<> ();
                                sectionsText.put ( delimiter.getSetionDelimiter (), sectionRows );

                                inSection = true;
                            } else {
                                // la linea non rientra tra i delimitatori e siamo fuori sezione...eccezione
                                throw new UnexpectedSectionException ( trimmedLine );
                            }
                        }
                    }
                    */

                } else {
                    // In caso di null si termina la lettura, niente piu' da leggere
                    done = true;
                }
            }

            EmailParserSectionDelimiter delimiter = sectionDelimiters.stream ()
                .filter ( del -> del.isRequired () && !sectionsText.containsKey ( del.getSetionDelimiter () ) )
                .findFirst ()
                .orElse ( null );

            if ( delimiter != null ) {
                throw new MissingRequiredSectionException ( delimiter.getSetionDelimiter () );
            }

        } catch ( IOException e ) {

            logger.error ( CLASS_NAME, METHOD_NAME_PARSE_EMAIL_BODY, "errore di parse", e );
            throw new GenericParseErrorException ( e );

        } finally {
            logger.end ( CLASS_NAME, METHOD_NAME_PARSE_EMAIL_BODY );
        }

        return sectionsText;
    }

    public static EmailRecipient parseRecipient ( String recipientStr ) throws IllegalMailRecipientFormat {
        logger.begin ( CLASS_NAME, METHOD_NAME_PARSE_EMAIL_RECIPIENT );
        EmailRecipient recipient = null;
        try {
            Matcher m = recipentPattern.matcher ( recipientStr );

            if ( m.matches () ) {
                recipient = new EmailRecipient ();
                recipient.nameSurname = m.group ( 1 ).trim ();
                recipient.emailAddress = m.group ( 2 );
            } else {
                logger.warn ( METHOD_NAME_PARSE_EMAIL_RECIPIENT, "IllegalMailRecipientFormat:" + recipientStr );
                throw new IllegalMailRecipientFormat ( recipientStr );
            }
        } finally {
            logger.end ( CLASS_NAME, METHOD_NAME_PARSE_EMAIL_RECIPIENT );
        }
        return recipient;
    }

    public static Map<String, List<String>> parseEmailHTMLBody ( String emailBody, Set<EmailParserSectionDelimiter> sectionDelimiters )
                    throws UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException {

        logger.begin ( CLASS_NAME, METHOD_NAME_PARSE_EMAIL_HTML_BODY );

        StringBuilder sb = new StringBuilder ();

        Document doc = Jsoup.parse ( emailBody );

        analyzeChildren ( sb, doc.childNodes (), null );

        logger.end ( CLASS_NAME, METHOD_NAME_PARSE_EMAIL_HTML_BODY );

        return parseEmailBody ( sb.toString (), sectionDelimiters );

    }

    private static void analyzeChildren ( StringBuilder sb, List<Node> children, Node nodeParent ) {
        for ( org.jsoup.nodes.Node child: children ) {
            if ( child.childNodeSize () > 0 ) {
                analyzeChildren ( sb, child.childNodes (), child );
            }

            if ( child instanceof TextNode ) {
            	String trimmedText = ( (TextNode) child ).text ();
            	
            	trimmedText = trimmedText.replace((char)160, (char)32);
            	
                sb.append ( StringUtils.trimWhitespace ( trimmedText ) );
            } else if ( child.nodeName ().equals ( "br" ) ) {
                sb.append ( "\r\n" );
            }
        }
        if ( ( null != nodeParent ) && "p".equals ( nodeParent.nodeName () ) ) {
            sb.append ( "\r\n" );
        }
    }

    /**
     *
     * @param subject
     * @return il codice del poll afferente all'oggeto della mail passato come input
     * @throws IllegalMailSubjectException se l'oggetto non soddisfa il pattern richiesto Metodo che, preso una stringa rappresentante l'oggetto di una mail ne
     *             restituisce il codice del poll
     */
    public static ParsingPollOutputDTO parsePollCodeFromSubject ( String subject ) throws IllegalMailSubjectException {

        logger.begin ( CLASS_NAME, METHOD_NAME_PARSE_POLL_CODE_FROM_SUBJECT );
        ParsingPollOutputDTO chiamata = new ParsingPollOutputDTO ();
        String pollCode = null;

        if ( ( null != subject ) && ( subject.trim ().length () > 0 ) ) {
            if ( subject.toLowerCase ().contains ( subjectStartString ) ) {
                Matcher m = Pattern.compile ( "(?i)(?<=" + subjectStartString + ").*$(?-i)" ).matcher ( subject );
                if ( m.find () ) {
                    chiamata.setCodicePoll ( m.group ( 0 ).trim () );
                    chiamata.setAzione ( subjectStartString );
                } else {
                    logger.info ( METHOD_NAME_PARSE_POLL_CODE_FROM_SUBJECT, "Oggetto per nuova votazione non coerente" );
                }
            } else if ( subject.toLowerCase ().contains ( subjectClosingString ) ) {
                Matcher m = Pattern.compile ( "(?i)(?<=" + subjectClosingString + ").*$(?-i)" ).matcher ( subject );
                if ( m.find () ) {
                    chiamata.setCodicePoll ( m.group ( 0 ).trim () );
                    chiamata.setAzione ( subjectClosingString );
                } else {
                    logger.info ( METHOD_NAME_PARSE_POLL_CODE_FROM_SUBJECT, "Oggetto per nuova votazione non coerente" );
                }
            } else if ( ( subject.indexOf ( ')' ) >= 0 ) && ( subject.indexOf ( '(' ) >= 0 ) ) {
                chiamata.setCodicePoll ( subject.substring ( subject.indexOf ( '(' ) + 1, subject.indexOf ( ')' ) ) );
                chiamata.setAzione ( subjectPreferenceString );
            } else {
                logger.info ( METHOD_NAME_PARSE_POLL_CODE_FROM_SUBJECT, "Oggetto generico non coerente" );
            }
            logger.info ( METHOD_NAME_PARSE_POLL_CODE_FROM_SUBJECT, String.format ( "poll code is %s", pollCode ) );
        } else {
            throw new IllegalMailSubjectException ( "null or empty" );
        }

        logger.end ( CLASS_NAME, METHOD_NAME_PARSE_POLL_CODE_FROM_SUBJECT );

        return chiamata;
    }

    public static String removeAllHTMLTags ( String source ) {

        StringBuilder sb = new StringBuilder ();

        Document doc = Jsoup.parse ( source );

        analyzeChildren ( sb, doc.childNodes (), null );

        return sb.toString ();

    }


    public static Set<EmailParserSectionDelimiter> getDelimitersNewPoll () {
        return sectionDelimitersNewPoll;
    }
}
