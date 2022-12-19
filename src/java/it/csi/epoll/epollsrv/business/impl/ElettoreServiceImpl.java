/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.ElettoreService;
import it.csi.epoll.epollsrv.integration.domain.Elettore;
import it.csi.epoll.epollsrv.integration.repository.ElettoreRepository;


/**
 * 
 */
@Service
@Transactional
public class ElettoreServiceImpl implements ElettoreService {

    @Autowired
    private ElettoreRepository elettoreRepository;

    @Override
    public ElettoreRepository getRepository () {
        return elettoreRepository;
    }

    @Override
    @Transactional ( readOnly = true )
    public Elettore cercaElettorePerIdPollEdEmail ( Long idPoll, String email ) {
        return elettoreRepository.findOneByPollIdAndEmailAndDtDataVotazioneIsNull ( idPoll, email );
    }

}
