/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.csi.epoll.epollsrv.integration.domain.BodyPreferenza;


@Repository
public interface BodyPreferenzaRepository extends IRepository<BodyPreferenza, String> {
	
	List<BodyPreferenza> findByPollId(Long pollId);

}
