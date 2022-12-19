/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.integration.repository.EnteRepository;


/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface EnteService extends IService<EnteRepository, Ente, Long> {

}
