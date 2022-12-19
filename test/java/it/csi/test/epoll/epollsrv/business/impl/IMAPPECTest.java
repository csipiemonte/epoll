/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.test.epoll.epollsrv.business.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;

import it.csi.epoll.epollsrv.business.dto.ConfigDTO;
import it.csi.test.epoll.pec.PecParser2;

/**
 *
 */

public class IMAPPECTest {

    @Test
    public void readMessages() {
        try { 
            Properties props = System.getProperties ();
            props.setProperty ( "mail.store.protocol", ConfigDTO.PROTOCOL_IMAP );
            props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".auth", "true" );
            props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".ssl.enable", "true" ); // attiva ssl e imposta la porta standard 993
            props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".starttls.enable", "true" );
            props.setProperty ( ConfigDTO.MAIL_PREFIX + ConfigDTO.PROTOCOL_IMAP + ".ssl.protocols", "TLSv1.2" );

            Session session = Session.getDefaultInstance ( props, null );
            Store store = session.getStore ( ConfigDTO.PROTOCOL_IMAP );
            store.connect ( "mbox.cert.legalmail.it", "M9187846", "ePoll77!");
            
            Folder inbox = store.getFolder( "INBOX" );
            inbox.open( Folder.READ_WRITE );
    
            // Fetch unseen messages from inbox folder
            Message[] messages = inbox.search(
                new FlagTerm(new Flags(Flags.Flag.SEEN), true));
    
            // Sort messages from recent to oldest
            Arrays.sort( messages, ( m1, m2 ) -> {
              try {
                return m2.getSentDate().compareTo( m1.getSentDate() );
              } catch ( MessagingException e ) {
                throw new RuntimeException( e );
              }
            } );
    
            for ( Message message : messages ) {
                MimeMessage mail = ( MimeMessage ) message;
                
                System.out.println ( mail.getSubject () );

                PecParser2 pecParser = new PecParser2 ();
                
                pecParser.dumpPart ( message );
                
                System.out.println ( "Oggetto: " + pecParser.getPostacertEmlSubject () );
                if (pecParser.getPostacertEmlBody () != null) {
                    System.out.println ( "Is Html: " + pecParser.getPostacertEmlBody ().isHtml () );
                    
                    StringBuilder sb = new StringBuilder ();
                    
                    if ( pecParser.getPostacertEmlBody ().isHtml () ) {
                        
                        Document doc = Jsoup.parse(pecParser.getPostacertEmlBody ().getBody ());
                        
                        analyzeChildren(sb, doc.childNodes ());
                        
                    }
                    
                    System.out.println ( sb.toString () );
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
            Assert.fail (e.getMessage ());
        }
    }

    private void analyzeChildren ( StringBuilder sb, List<Node> children ) {
        for (org.jsoup.nodes.Node child : children) {
            if (child.childNodeSize () > 0) {
                analyzeChildren (sb, child.childNodes () );
            }
            
            if (child instanceof TextNode) {
                sb.append ( ((TextNode)child).text () );
            } else if (child.nodeName ().equals ( "br" ) ) {
                sb.append ( "\r\n" );
            }
        }
    }
}
