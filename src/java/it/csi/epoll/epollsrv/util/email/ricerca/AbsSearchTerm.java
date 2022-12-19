/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.ricerca;

import javax.mail.search.SearchTerm;

import org.springframework.util.Assert;

import it.csi.epoll.epollsrv.util.ErrorMessages;


/**
 * @author abora
 * 
 * 
 *         Classe astratta da fare estendere a tutte le classi concrete usate per i filtri di ricerca. Serve per il multiente.
 */
@SuppressWarnings ( "serial" )
public abstract class AbsSearchTerm extends SearchTerm {

    /**
     * Identificativo univoco dell'ente utile per identificare la casella di posta
     */
    private Long idEnte;

    /**
     * @param idEnte
     */
    protected AbsSearchTerm ( Long idEnte ) {
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

    /**
     * @param idEnte the idEnte to set
     */
    public void setIdEnte ( Long idEnte ) {
        this.idEnte = idEnte;
    }

    @Override
    public String toString () {
        return String.format ( "AbsSearchTerm [idEnte=%s]", idEnte );
    }

}
