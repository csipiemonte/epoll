/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import java.util.List;

import it.csi.epoll.epollsrv.integration.domain.BodyPreferenza;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.repository.BodyPreferenzaRepository;


public interface BodyPreferenzaService extends IService<BodyPreferenzaRepository, BodyPreferenza, String> {
	
	List<BodyPreferenza> cercaPreferenzeByPollId(String codicePoll);

	void salvaPreferenza(String bodyPref, Poll poll);
	
}
