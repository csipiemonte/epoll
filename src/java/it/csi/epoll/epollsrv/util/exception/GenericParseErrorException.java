/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.exception;


/**
 * @author lfantini Classe che definisce l'eccezione che viene lanciata quando si verifica un generico errore in fase di parsing di un messaggio
 */

public class GenericParseErrorException extends Exception {

    private static final long serialVersionUID = 2908808853705106710L;

    public GenericParseErrorException ( Exception e ) {
        super ( e );
    }

}
