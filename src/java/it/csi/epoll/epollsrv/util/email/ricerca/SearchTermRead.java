/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.ricerca;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

import it.csi.epoll.epollsrv.util.Constants;
import it.csi.epoll.epollsrv.util.logger.EpollsrvLogger;
import it.csi.epoll.epollsrv.util.logger.LogCategory;


/**
 * @author abora
 * 
 * 
 *         Classe usata come filtro di ricerca per avere tutti i messaggi nuovi( Non letti ne' dal client imap e ne' dal client di posta)
 */
public class SearchTermRead extends AbsSearchTerm {

    private static final EpollsrvLogger LOGGER = new EpollsrvLogger ( LogCategory.EPOLLSRV_ROOT_LOG_CATEGORY_UTIL_LOG_CATEGORY, "SearchTermRead" );

    /**
     *
     */
    private static final long serialVersionUID = -7016162501526401250L;

    /**
     * @param idEnte
     */
    public SearchTermRead ( Long idEnte ) {
        super ( idEnte );
    }

    @Override
    public boolean match ( Message msg ) {
        String methodName = "match";
        try {
            /**
             * !msg.isExpunged (): server per escludere messaggi marcati come eliminati o non più presenti. Se eliminato si otterrà una exception
             * 
             * !msg.isSet ( Flags.Flag.DELETED ): non prendo in condiserazione messaggi marcati come eliminati.
             * 
             * !msg.isSet ( Flags.Flag.RECENT ): prendo in considerazione solo i messaggi nuovi ovvero solo quelli che sono arrivati dopo l'accesso alla email
             * tramite ricerca.
             * 
             * !msg.isSet ( Flags.Flag.SEEN ): prendo in considerazione solo i messaggi nuovi anche dopo accessi alla casella email.
             */
            if ( !msg.isExpunged () && !msg.isSet ( Flags.Flag.DELETED ) && ( msg.isSet ( Flags.Flag.RECENT ) || !msg.isSet ( Flags.Flag.SEEN ) ) ) {// significa che il messaggio e' nuovo

                String [] xRicevuta = msg.getHeader ( Constants.E_HEADER_X_RICEVUTA );

                // verifico che il messaggio sia quello opportuno 
                if ( ( xRicevuta != null ) && ( xRicevuta.length > 0 ) ) {
                    if ( !Constants.E_TIPO_MAIL_RICEVUTA_CONSEGNA.equals ( xRicevuta [0] )
                        && !Constants.E_TIPO_MAIL_MANCATA_CONSEGNA.equals ( xRicevuta [0] )
                        && !Constants.E_TIPO_MAIL_PRESA_IN_CARICO.equals ( xRicevuta [0] ) ) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    // se non ha l'header e' quello di interesse
                    return true;
                }
            } else {
                return false;
            }
        } catch ( MessagingException e ) {
            LOGGER.error ( methodName, "Errore in fase di ricerca dei messaggi nuovi", e );
            return false;
        }
    }

}
