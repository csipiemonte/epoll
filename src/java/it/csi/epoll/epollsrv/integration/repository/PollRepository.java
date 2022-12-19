/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.repository;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.StatoPoll;


@Repository
public interface PollRepository extends IRepository<Poll, Long> {

    Poll findByCodice ( String codice );

    Poll findByCodiceAndDtInizioValiditaBeforeAndDtFineValiditaAfter ( String codice, Date now1, Date now2 );

    List<Poll> findAllByStatoPollAndDtFineValiditaBefore ( StatoPoll statoPoll, Date now );

    List<Poll> findAllByStatoPollAndFrequenzaAggiornamentoGreaterThanEqual ( StatoPoll statoPoll, Long frequenzaInvio );

}
