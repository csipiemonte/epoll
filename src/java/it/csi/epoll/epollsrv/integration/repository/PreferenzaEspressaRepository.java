/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.repository;

import org.springframework.stereotype.Repository;

import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.PreferenzaEspressa;


@Repository
public interface PreferenzaEspressaRepository extends IRepository<PreferenzaEspressa, String> {

    PreferenzaEspressa findOneByPollAndValidoAndPreferenzaIgnoreCase ( Poll poll, boolean valido, String preferenza );

}
