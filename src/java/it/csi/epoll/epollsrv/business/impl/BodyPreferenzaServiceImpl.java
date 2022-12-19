/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.business.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.BodyPreferenzaService;
import it.csi.epoll.epollsrv.integration.domain.BodyPreferenza;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.repository.BodyPreferenzaRepository;
import it.csi.epoll.epollsrv.integration.repository.PollRepository;

@Service
@Transactional
public class BodyPreferenzaServiceImpl implements BodyPreferenzaService {
	
	@Autowired
	private BodyPreferenzaRepository bodyPreferenzaRepository;
	
	@Autowired
	private PollRepository pollRepository;
	
	@Override
	public BodyPreferenzaRepository getRepository() {
		return bodyPreferenzaRepository;
	}

	@Override
	public List<BodyPreferenza> cercaPreferenzeByPollId(String codicePoll) {
		
		Long pollId = pollRepository.findByCodice(codicePoll).getId(); 
		
		return bodyPreferenzaRepository.findByPollId(pollId);
	}
	
	@Override
	public void salvaPreferenza (String bodyPref, Poll poll) {
		BodyPreferenza body = new BodyPreferenza();
        body.setUuid ( UUID.randomUUID ().toString () );
		body.setBodyPreferenza(bodyPref);
		body.setPoll(poll);
		bodyPreferenzaRepository.save(body);
	}
	

}
