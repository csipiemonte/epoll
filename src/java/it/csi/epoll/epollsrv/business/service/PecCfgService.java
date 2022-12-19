/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import it.csi.epoll.epollsrv.integration.domain.PecCfg;
import it.csi.epoll.epollsrv.integration.repository.PecCfgRepository;


/**
 * Servizio per la gestione della configurazonie della pec
 */
public interface PecCfgService extends IService<PecCfgRepository, PecCfg, Long> {

    /**
     * Trova la configurazione della email per id ente.
     * 
     * @param idEnte
     * @return PecCfg
     */
    PecCfg findOneByEnteId ( Long idEnte );
}
