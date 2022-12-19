/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;


/**
 *
 */

public class EnteDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    private String codFiscale;

    private String codice;

    private String descrizione;

    public String getCodFiscale () {
        return codFiscale;
    }

    public void setCodFiscale ( String codFiscale ) {
        this.codFiscale = codFiscale;
    }

    public String getCodice () {
        return codice;
    }

    public void setCodice ( String codice ) {
        this.codice = codice;
    }

    public String getDescrizione () {
        return descrizione;
    }

    public void setDescrizione ( String descrizione ) {
        this.descrizione = descrizione;
    }

    /**
     * @return the id
     */
    public Long getId () {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId ( Long id ) {
        this.id = id;
    }

    @Override
    public String toString () {
        return String.format ( "EnteDTO [id=%s, codFiscale=%s, codice=%s, descrizione=%s]", id, codFiscale, codice, descrizione );
    }

}
