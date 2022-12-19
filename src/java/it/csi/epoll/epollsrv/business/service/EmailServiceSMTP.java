/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;


/**
 * @author abora
 * 
 * 
 *         Servizio che gestisce il protocollo smtp, principalmente sendEmail
 */

public interface EmailServiceSMTP extends EmailService {

    /**
     * Servizio per l'invio di una mail. Valorizzare sempre tutti i parametri obbligatori.
     * 
     * @param message obbligatorio
     * @param message.idEnte obbligatorio
     * @param message.to obbligatorio
     * @param message.from obbligatorio
     * @param message.body obbligatorio
     * @param message.oggetto obbligatorio
     * @param message.headers obbligatorio
     * 
     * @return true if sent otherwise false.
     * @exception IllegalArgumentException if the object is <code>null</code>
     */
    Boolean sendMail ( EmailDTO message );

    @Override
    default Boolean isActive () {
        return Boolean.TRUE;
    }

}
