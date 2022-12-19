/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import it.csi.epoll.epollsrv.integration.domain.Poll;

/**
 * @author lfantini Classe che definisce un messaggio di tipo "nuova votazione"
 */

public class PollParsedIncomingMessage extends ParsedIncomingMessage {

    private static final long serialVersionUID = -3500890590094558198L;

    private Poll poll;

    public Poll getPoll () {
        return poll;
    }

    public void setPoll ( Poll poll ) {
        this.poll = poll;
    }


}
