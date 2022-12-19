/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.service;

import java.io.IOException;

import it.csi.epoll.epollsrv.business.dto.email.EmailDTO;
import it.csi.epoll.epollsrv.business.dto.email.ParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.dto.email.PreferenzaEspressaParsedIncomingMessage;
import it.csi.epoll.epollsrv.business.dto.email.PreferenzeEspresseParsedIncomingMessage;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.util.exception.GenericParameterException;
import it.csi.epoll.epollsrv.util.exception.GenericParseErrorException;
import it.csi.epoll.epollsrv.util.exception.IllegalMailSubjectException;
import it.csi.epoll.epollsrv.util.exception.MissingRequiredSectionException;
import it.csi.epoll.epollsrv.util.exception.NotEnoughPreferenzeException;
import it.csi.epoll.epollsrv.util.exception.NotValidDateException;
import it.csi.epoll.epollsrv.util.exception.UnexpectedSectionException;


/**
 * @author lfantini Servizio di parsing delle mail
 */

public interface ParsingService extends MonitoringService {

    /**
     * Parsing di un messaggio in entrata
     *
     * @param emailDTO
     * @return il messaggio incapsulato all'interno di una sottoclasse di ParsedIncomingMessage
     */
    ParsedIncomingMessage parseIncomingMessage ( EmailDTO emailDTO )
                    throws IllegalMailSubjectException, IOException, UnexpectedSectionException, MissingRequiredSectionException, GenericParseErrorException,
                    NotValidDateException, NotEnoughPreferenzeException, GenericParameterException;

    @Override
    default Boolean isActive () {
        return true;
    }

	PreferenzaEspressaParsedIncomingMessage parsePreferenzaEspressa(Poll existingPoll, EmailDTO emailDTO);

	PreferenzeEspresseParsedIncomingMessage parsePreferenzeEspresse(Poll existingPoll, EmailDTO emailDTO);
}
