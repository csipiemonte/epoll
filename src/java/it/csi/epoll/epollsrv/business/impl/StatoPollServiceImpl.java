/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.epoll.epollsrv.business.service.StatoPollService;
import it.csi.epoll.epollsrv.integration.domain.StatoPoll;
import it.csi.epoll.epollsrv.integration.repository.StatoPollRepository;
import it.csi.epoll.epollsrv.util.ErrorMessages;


/**
 *
 */
@Service
@Transactional
public class StatoPollServiceImpl implements StatoPollService {

    @Autowired
    private StatoPollRepository statoPollRepository;

    @Override
    public StatoPollRepository getRepository () {
        return statoPollRepository;
    }

    /**
     * Operazione di salvataggio non permesso su questa tabella
     */
    @SuppressWarnings ( "unchecked" )
    @Override
    public StatoPoll save ( StatoPoll entity ) {
        throw new UnsupportedOperationException ( ErrorMessages.G_OPERAZIONE_NON_CONSENTITA );
    }

    /**
     * Operazione di delete non permesso su questa tabella
     */
    @Override
    public void delete ( String id ) {
        throw new UnsupportedOperationException ( ErrorMessages.G_OPERAZIONE_NON_CONSENTITA );
    }
}
