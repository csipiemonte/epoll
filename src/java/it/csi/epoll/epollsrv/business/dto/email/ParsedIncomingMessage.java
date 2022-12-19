/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import java.io.Serializable;


/**
 * @author lfantini Classe che definisce un messaggio (parsed) e contiene tutti i field comuni
 */

public abstract class ParsedIncomingMessage implements Serializable {

    private static final long serialVersionUID = 3566908577453054248L;

    private String mittente;
    private Boolean isMittentePresidente;
    private String codicePoll;

    public String getMittente () {
        return mittente;
    }

    public void setMittente ( String mittente ) {
        this.mittente = mittente;
    }

    public Boolean getIsMittentePresidente () {
        return isMittentePresidente;
    }

    public void setIsMittentePresidente ( Boolean isMittentePresidente ) {
        this.isMittentePresidente = isMittentePresidente;
    }

    public String getCodicePoll () {
        return codicePoll;
    }

    public void setCodicePoll ( String codicePoll ) {
        this.codicePoll = codicePoll;
    }

}
