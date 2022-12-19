/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util;

/**
 *
 */

public enum Environments {
        LOCAL ( "local" ),
        DEV ( "dev" ),
        TEST ( "tst-rpcr-01" ),
        COLL ( "coll-rpcr-01" ),
        PROD ( "prod-rpcr-01" );

    private String codice;

    public String getCodice () {
        return codice;
    }

    private Environments ( String codice ) {
        this.codice = codice;
    }

    public static Environments fromCodice ( String codice ) {
        for (Environments candidate : Environments.values()) {
            if (candidate.getCodice ().equals ( codice )) {
                return candidate;
            }
        }
        return null;
    }

}
