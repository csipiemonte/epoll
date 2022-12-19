/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.dto.PreferenzaDaValidareDTO;
import it.csi.epoll.epollsrv.business.service.PreferenzaEspressaService;
import it.csi.epoll.epollsrv.integration.domain.BodyPreferenza;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.PreferenzaEspressa;
import it.csi.epoll.epollsrv.integration.repository.BodyPreferenzaRepository;
import it.csi.epoll.epollsrv.integration.repository.PreferenzaEspressaRepository;


/**
 *
 */
@Service
@Transactional
public class PreferenzaEspressaServiceImpl implements PreferenzaEspressaService {

    @Autowired
    private PreferenzaEspressaRepository preferenzaEspressaRepository;
    
    @Autowired
    private BodyPreferenzaRepository bodyPreferenzaRepository;

    @Override
    public PreferenzaEspressaRepository getRepository () {
        return preferenzaEspressaRepository;
    }

    @Override
    @Transactional ( propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class )
    public PreferenzaEspressa salvaPreferenza ( Poll poll, boolean valido, String preferenza ) {
        PreferenzaEspressa preferenzaEspressa = preferenzaEspressaRepository.findOneByPollAndValidoAndPreferenzaIgnoreCase ( poll, valido, preferenza );
        if ( preferenzaEspressa != null ) {
            preferenzaEspressa.setConteggio ( preferenzaEspressa.getConteggio () + 1 );
        } else {
            preferenzaEspressa = new PreferenzaEspressa ();
            preferenzaEspressa.setConteggio ( 1 );
            preferenzaEspressa.setPoll ( poll );
            preferenzaEspressa.setPreferenza ( preferenza );
            preferenzaEspressa.setValido ( valido );
            preferenzaEspressa.setUuid ( UUID.randomUUID ().toString () );
        }
        
        return preferenzaEspressaRepository.save ( preferenzaEspressa );
    }

	@Override
	@Transactional ( propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class )
	public List<PreferenzaEspressa> salvaPreferenze(Poll existingPoll, List<PreferenzaDaValidareDTO> preferenzeDaValidare) {
		
		List<PreferenzaEspressa> preferenze = new LinkedList<>();
		for(PreferenzaDaValidareDTO preferenzaValidata : preferenzeDaValidare) {
			PreferenzaEspressa preferenzaEspressa = preferenzaEspressaRepository.findOneByPollAndValidoAndPreferenzaIgnoreCase ( existingPoll, preferenzaValidata.isValida(), preferenzaValidata.getPreferenza() );
			 if ( preferenzaEspressa != null ) {
		            preferenzaEspressa.setConteggio ( preferenzaEspressa.getConteggio () + 1 );
		        } else {
		            preferenzaEspressa = new PreferenzaEspressa ();
		            preferenzaEspressa.setConteggio ( 1 );
		            preferenzaEspressa.setPoll ( existingPoll );
		            preferenzaEspressa.setPreferenza ( preferenzaValidata.getPreferenza() );
		            preferenzaEspressa.setValido ( preferenzaValidata.isValida() );
		            preferenzaEspressa.setUuid ( UUID.randomUUID ().toString () );
		        }
			 preferenze.add(preferenzaEspressa);
			 
		}

		return preferenzaEspressaRepository.save(preferenze);
	}

}
