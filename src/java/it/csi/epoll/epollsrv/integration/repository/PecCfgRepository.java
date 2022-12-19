/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.repository;

import org.springframework.stereotype.Repository;

import it.csi.epoll.epollsrv.integration.domain.PecCfg;


@Repository
public interface PecCfgRepository extends IRepository<PecCfg, Long> {

    /**
     * Trova la configurazione della email per id ente.
     * 
     * @param idEnte
     * @return PecCfg
     */
    PecCfg findOneByEnteId ( Long idEnte );

    /**
     * Trova la configurazione della email per codice fiscale.
     * 
     * @param codFiscale
     * @return PecCfg
     */
    PecCfg findOneByEnteCodFiscale ( String codFiscale );
}
