/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.PreferenzaService;
import it.csi.epoll.epollsrv.integration.repository.PreferenzaRepository;


/**
 * 
 */
@Service
@Transactional
public class PreferenzaServiceImpl implements PreferenzaService {
	
    @Autowired
    private PreferenzaRepository preferenzaRepository;

    @Override
    public PreferenzaRepository getRepository () {
        return preferenzaRepository;
    }

   
}
