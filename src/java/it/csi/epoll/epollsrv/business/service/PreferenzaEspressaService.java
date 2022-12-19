/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import java.util.List;

import it.csi.epoll.epollsrv.business.dto.PreferenzaDaValidareDTO;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.PreferenzaEspressa;
import it.csi.epoll.epollsrv.integration.repository.PreferenzaEspressaRepository;


/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface PreferenzaEspressaService extends IService<PreferenzaEspressaRepository, PreferenzaEspressa, String> {

    /**
     * Servizio che gestisce il voto con una transazione nuova. Da usare con cautela
     * 
     * @param poll
     * @param valido
     * @param preferenza
     * @return
     */
    PreferenzaEspressa salvaPreferenza ( Poll poll, boolean valido, String preferenza );

	List<PreferenzaEspressa> salvaPreferenze(Poll existingPoll, List<PreferenzaDaValidareDTO> preferenzeDaValidare);

}
