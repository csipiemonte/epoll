/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.ricerca;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.util.Assert;

import it.csi.epoll.epollsrv.util.ErrorMessages;


/**
 * @author abora
 * 
 * 
 *         Classe astratta da fare estendere a tutte le classi concrete usate per i filtri di ricerca. Serve per il multiente.
 */
public class EPollMessage extends MimeMessage {

    /**
     * Identificativo univoco dell'ente utile per identificare la casella di posta
     */
    private Long idEnte;

    /**
     * @param idEnte
     * @throws MessagingException
     */
    public EPollMessage ( MimeMessage mimeMessage, Long idEnte ) throws MessagingException {
        super ( mimeMessage );
        Assert.notNull ( idEnte, ErrorMessages.G_PARAMETRO_ENTE_OBBLIGATORIO_NON_SPECIFICATO );
        this.idEnte = idEnte;
    }

    /**
     * @return the idEnte
     */
    public Long getIdEnte () {
        return idEnte;
    }

    /**
     * @param idEnte the idEnte to set
     */
    public void setIdEnte ( Long idEnte ) {
        this.idEnte = idEnte;
    }

    @Override
    public String toString () {
        return String.format ( "AbsMessage [idEnte=%s]", idEnte );
    }

}
