/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import java.util.List;

import it.csi.epoll.epollsrv.business.dto.RisultatiNotifichePollDTO;
import it.csi.epoll.epollsrv.business.dto.RisultatiScrutinioPollDTO;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.repository.PollRepository;


/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface PollService extends IService<PollRepository, Poll, Long> {

    /**
     * Cerca il poll per codice.
     * 
     * @param codicePoll condice univoc poll
     * @return poll se presente
     */
    Poll cercaPollPerCodice ( String codicePoll );

    List<Poll> cercaPollDaScrutinare ();

    RisultatiScrutinioPollDTO scrutinaPoll ( Poll poll );

    RisultatiNotifichePollDTO notificaPoll ( Poll poll );

    List<RisultatiScrutinioPollDTO> cercaEScrutinaPoll ();

    List<RisultatiNotifichePollDTO> cercaPerInvioAggiornamento ();

}
