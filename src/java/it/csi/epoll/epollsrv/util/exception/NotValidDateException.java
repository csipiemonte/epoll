/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.exception;

public class NotValidDateException extends Exception {

    private static final long serialVersionUID = -214718661195616852L;

    private final String message;

    public NotValidDateException ( String message ) {
        this.message = message;
    }

    @Override
    public String getMessage () {
        return message;
    }

}
