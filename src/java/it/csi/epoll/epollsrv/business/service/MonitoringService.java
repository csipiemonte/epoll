/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;


/**
 * @author abora
 * 
 * 
 *         Da utilizzare per la messa in monitoraggio dei servizi desiderati.
 */
public interface MonitoringService {

    /**
     * Servizio di test
     * 
     * @return true if active otherwise false.
     */
    Boolean isActive ();
}
