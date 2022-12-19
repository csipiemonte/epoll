/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;


/**
 * @author lfantini Enum che rappresenta gli stati di un Poll
 */

public enum StatoPollEnum implements Serializable {

        CREATO ( "C", "Creato" ),
        SCRUTINATO ( "S", "Scrutinato" );

    private String codStato;

    private String descrizione;

    private StatoPollEnum ( String codStato, String descrizione ) {
        this.codStato = codStato;
        this.descrizione = descrizione;
    }

    public String getCodStato () {
        return codStato;
    }

    public String getDescrizione () {
        return descrizione;
    }

}
