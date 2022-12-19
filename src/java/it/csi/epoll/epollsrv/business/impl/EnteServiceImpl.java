/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.EnteService;
import it.csi.epoll.epollsrv.integration.repository.EnteRepository;


/**
 * 
 */
@Service
@Transactional
public class EnteServiceImpl implements EnteService {
	
    // scommentare al bisogno
    // private static final AttiudpboLogger logger = new AttiudpboLogger ( LogCategory.ATTIUDPBO_BUSINESS_LOG_CATEGORY, "EnteServiceImpl" )

    @Autowired
    private EnteRepository enteRepository;

    @Override
    public EnteRepository getRepository () {
        return enteRepository;
    }

   
}
