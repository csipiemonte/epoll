/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;
import java.util.Date;


/**
 *
 */

public class ElettoreDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String cognomeNome;

    private Date dtDataVotazione;

    private String email;

    public String getCognomeNome () {
        return cognomeNome;
    }

    public void setCognomeNome ( String cognomeNome ) {
        this.cognomeNome = cognomeNome;
    }

    public Date getDtDataVotazione () {
        return dtDataVotazione;
    }

    public void setDtDataVotazione ( Date dtDataVotazione ) {
        this.dtDataVotazione = dtDataVotazione;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    @Override
    public String toString () {
        return "ElettoreDTO [cognomeNome=" + cognomeNome
            + ", dtDataVotazione=" + dtDataVotazione
            + ", email=" + email
            + "]";
    }

}
