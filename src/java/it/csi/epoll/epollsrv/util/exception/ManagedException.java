/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.exception;

/**
 *
 */

public class ManagedException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -6890430259292638910L;

    private static final int DEFAULT_STATUS = 500;

    private static final String DEFAULT_CODE = "INTERNAL";

    private int status;

    private String codice;

    public ManagedException ( String message ) {
        super ( message );
        status = DEFAULT_STATUS;
        codice = DEFAULT_CODE;
    }

    public ManagedException ( String codice, String message ) {
        super ( message );
        status = DEFAULT_STATUS;
        this.codice = codice;
    }

    public ManagedException ( String message, Throwable cause ) {
        super ( message, cause );
        status = DEFAULT_STATUS;
        codice = DEFAULT_CODE;
    }

    public ManagedException ( int status, String message, Throwable cause ) {
        super ( message, cause );
        this.status = status;
        codice = DEFAULT_CODE;
    }

    public ManagedException ( int status, String codice, String message, Throwable cause ) {
        super ( message, cause );
        this.status = status;
        this.codice = codice;
    }

    public ManagedException ( String codice, String message, Throwable cause ) {
        super ( message, cause );
        status = DEFAULT_STATUS;
        this.codice = codice;
    }

    public int getStatus () {
        return status;
    }

    public String getCodice () {
        return codice;
    }

}
