/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.repository;

import org.springframework.stereotype.Repository;

import it.csi.epoll.epollsrv.integration.domain.AlgoritmoVotazione;


@Repository
public interface AlgoritmoVotazioneRepository extends IRepository<AlgoritmoVotazione, String> {

}
