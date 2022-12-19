/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;
import java.util.List;


/**
 *
 */

public class PresidenteDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String cognomeNome;

    private String email;

    private List<PollDTO> polls;

    private EnteDTO ente;

    public String getCognomeNome () {
        return cognomeNome;
    }

    public void setCognomeNome ( String cognomeNome ) {
        this.cognomeNome = cognomeNome;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public List<PollDTO> getPolls () {
        return polls;
    }

    public void setPolls ( List<PollDTO> polls ) {
        this.polls = polls;
    }

    public EnteDTO getEnte () {
        return ente;
    }

    public void setEnte ( EnteDTO ente ) {
        this.ente = ente;
    }

    @Override
    public String toString () {
        return "PresidenteDTO [cognomeNome=" + cognomeNome + ", email=" + email + ", polls=" + polls + ", ente=" + ente + "]";
    }

}
