/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.repository;

import org.springframework.stereotype.Repository;

import it.csi.epoll.epollsrv.integration.domain.StatoPoll;


@Repository
public interface StatoPollRepository extends IRepository<StatoPoll, String> {

}
