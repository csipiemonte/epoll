/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;

/**
 *
 */

public class ParsingPollOutputDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String azione;

    private String codicePoll;

    public String getAzione () {
        return azione;
    }

    public void setAzione ( String azione ) {
        this.azione = azione;
    }

    public String getCodicePoll () {
        return codicePoll;
    }

    public void setCodicePoll ( String codicePoll ) {
        this.codicePoll = codicePoll;
    }


    @Override
    public String toString () {
        return "ParsingPollOutputDTO [azione=" + azione + ", codicePoll=" + codicePoll + "]";
    }


}
