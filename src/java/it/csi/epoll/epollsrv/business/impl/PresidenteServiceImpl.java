/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.PresidenteService;
import it.csi.epoll.epollsrv.integration.domain.Presidente;
import it.csi.epoll.epollsrv.integration.repository.PresidenteRepository;


/**
 *
 */
@Service
@Transactional
public class PresidenteServiceImpl implements PresidenteService {

    @Autowired
    private PresidenteRepository presidenteRepository;

    @Override
    public PresidenteRepository getRepository () {
        return presidenteRepository;
    }

    @Override
    @Transactional ( readOnly = true )
    public Presidente cercaPresidenteDaEmail ( String email ) {
        return presidenteRepository.findByEmail ( email );
    }

}
