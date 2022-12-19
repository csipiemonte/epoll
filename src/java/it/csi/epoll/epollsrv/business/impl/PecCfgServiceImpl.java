/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.PecCfgService;
import it.csi.epoll.epollsrv.integration.domain.PecCfg;
import it.csi.epoll.epollsrv.integration.repository.PecCfgRepository;


/**
 * 
 */
@Service
@Transactional
public class PecCfgServiceImpl implements PecCfgService {
	
    @Autowired
    private PecCfgRepository pecCfgRepository;

    @Override
    public PecCfgRepository getRepository () {
        return pecCfgRepository;
    }

    @Override
    @Transactional ( readOnly = true )
    public PecCfg findOneByEnteId ( Long idEnte ) {
        return pecCfgRepository.findOneByEnteId ( idEnte );
    }
   
}
