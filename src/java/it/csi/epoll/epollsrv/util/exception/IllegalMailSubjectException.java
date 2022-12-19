/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.exception;


/**
 * @author lfantini Classe che definisce un'eccezione lanciata quando l'oggetto della mail non soddisfa le caratteristiche richieste
 */

public class IllegalMailSubjectException extends Exception {

    private static final long serialVersionUID = -214718661195616852L;

    private final String invalidSubject;

    public IllegalMailSubjectException ( String subject ) {
        this.invalidSubject = subject;
    }

    public String getInvalidSubject () {
        return invalidSubject;
    }

}
