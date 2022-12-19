/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.email.ricerca.AbsSearchTerm;


/**
 * @author abora
 *
 *
 *         Servizio che gestisce il protocollo imap, ricerca, cancellazione etc.
 */
public interface EmailServiceIMAP extends EmailService {

    /**
     * Funzione che permette di settare all'email passata come parametro il flag DELETED a true. Non è richiesta nessuna connessione alla casella di posta
     * specifica. Nessuna operazione viene eseguita finchè non viene chiusa la cartella e lo store della casella di posta.
     * 
     * Anche se l'operazione viene permessa senza la verifica della connessione, in caso di delete con la connessione chiusa l'operazione risulterà corretta ma
     * non avrà alcun effetto.
     *
     * @param msg email da eliminare
     * @return false in caso di problemi true altrimenti.
     */
    boolean deleteMail ( Message msg );

    /**
     * Metodo che cerca nella cartella inbox secondo il criterio passata.
     *
     * @param search criterio di ricerca
     * @return List di email corrispondenti al criterio specificato
     */
    List<EmailDTO> searchMail ( AbsSearchTerm search );

    /**
     * Servizio di disconessione alla mail e cancellazione dei messaggi da cancellare.
     * 
     * @param idEnte identificativo configurazione casella email
     */
    void disconnectMail ( Long idEnte );

    /**
     * Metodo che restituisce tutti gli indirizzi email della tipologia specificata
     * 
     * @param msg email
     * @param recipientType tipologia di email(TO, CC, FROM)
     * @return lista di email di tipo String
     * @throws MessagingException
     */
    static List<String> extractRecipientType ( Message msg, RecipientType recipientType ) throws MessagingException {
        List<String> emailAdress = new LinkedList<> ();
        Address [] arrayAdress = msg.getRecipients ( recipientType );
        if ( null != arrayAdress ) {
            for ( Address address: arrayAdress ) {
                if ( address instanceof InternetAddress ) {
                    emailAdress.add ( ( (InternetAddress) address ).getAddress () );
                } else {
                    emailAdress.add ( extractEmail ( address.toString () ) );
                }
            }
        }
        return emailAdress;
    }

    /**
     * Metodo che permette di estrarre l'indirizzo email
     *
     * @param contentAddress stringa di partenza per avere l'indirizzo email
     * @return indirizzo email
     */
    static String extractEmail ( String contentAddress ) {
        Matcher matcher = Pattern.compile ( Constants.E_REGEX_EXTRACT_ADDRESS ).matcher ( contentAddress );
        if ( matcher.find () ) {
            return matcher.group ();
        } else {
            return contentAddress;
        }
    }

    /**
     * 
     *
     * @param message
     * @param idEnte
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    EmailDTO parseEmailToDTO ( Message message, Long idEnte ) throws MessagingException, IOException;

    @Override
    default Boolean isActive () {
        return Boolean.TRUE;
    }
}
