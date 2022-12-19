/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import it.csi.epoll.epollsrv.integration.domain.Elettore;
import it.csi.epoll.epollsrv.integration.repository.ElettoreRepository;


/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface ElettoreService extends IService<ElettoreRepository, Elettore, Long> {

    /**
     * Ricerca l'elettore tra gli elettori specificati e che non ha ancora votato: dt_votazione is null.
     * 
     * @param idPoll identificativo poll
     * @param email email votante
     * @return elettore
     */
    Elettore cercaElettorePerIdPollEdEmail ( Long idPoll, String email );
}
