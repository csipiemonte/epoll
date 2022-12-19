/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import it.csi.epoll.epollsrv.integration.domain.StatoPoll;
import it.csi.epoll.epollsrv.integration.repository.StatoPollRepository;


/**
 * Servizio per la consultazione della tabella di stato. Non sono permesse operazioni di modifica
 */
public interface StatoPollService extends IService<StatoPollRepository, StatoPoll, String> {

}
