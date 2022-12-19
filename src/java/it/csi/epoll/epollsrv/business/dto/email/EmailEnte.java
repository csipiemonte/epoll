/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import java.io.Serializable;

import org.springframework.util.Assert;

import it.csi.epoll.epollsrv.util.ErrorMessages;


/**
 * @author abora
 * 
 * 
 *         Classe da estendere a tutti i DTO usati per le operazioni con le caselle di posta. Serve a gestire il multiente
 */
abstract class EmailEnte implements Serializable {

    private Long idEnte;
    /**
     *
     */
    private static final long serialVersionUID = 5767631408832166716L;


    /**
     * @param idEnte
     */
    protected EmailEnte ( Long idEnte ) {
        super ();
        Assert.notNull ( idEnte, ErrorMessages.G_PARAMETRO_ENTE_OBBLIGATORIO_NON_SPECIFICATO );
        this.idEnte = idEnte;
    }

    /**
     * @return the idEnte
     */
    public Long getIdEnte () {
        return idEnte;
    }

    @Override
    public String toString () {
        return String.format ( "EMailEnte [idEnte=%s]", idEnte );
    }

}
