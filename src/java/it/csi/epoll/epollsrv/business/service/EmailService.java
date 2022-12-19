/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

/**
 * @author abora
 * 
 * 
 *         Operazioni comuni con le caselle email. TRANSAZIONE NOT_SUPPORTED SU TUTTI I SERVICE CHE IMPLEMENTANO QUESTA INTERFACE
 */
public interface EmailService extends MonitoringService {

    /**
     * Servizio di connessione alla mail
     */
    void connectMail ();

    /**
     *
     * @return true if is connected false otherwise
     */
    boolean isConnected ();

}
