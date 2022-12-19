/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import it.csi.epoll.epollsrv.integration.domain.Elettore;


@Repository
public interface ElettoreRepository extends IRepository<Elettore, Long> {

    /**
     * Metodo che cerca il lettore per idPoll ed email tenda di acquisire il lock in lettura e scrittura per evitare la registrazione di più voti per ogni
     * elettore. Se non ottiene il lock aspetta finchè non si libera la risorsa oppure va in timeout.
     * 
     * @param idPoll identificativo del poll
     * @param emai indirizzo email dell'elettore
     * @return elettore
     */
    @Lock ( LockModeType.PESSIMISTIC_WRITE )
    Elettore findOneByPollIdAndEmailAndDtDataVotazioneIsNull ( Long idPoll, String email );
}
