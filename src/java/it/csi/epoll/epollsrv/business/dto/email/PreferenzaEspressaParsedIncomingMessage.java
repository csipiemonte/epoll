/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import it.csi.epoll.epollsrv.integration.domain.PreferenzaEspressa;

/**
 * @author lfantini Classe che definisce un messaggio (parsed) relativo a una nuova preferenza
 */

public class PreferenzaEspressaParsedIncomingMessage extends ParsedIncomingMessage {

    private static final long serialVersionUID = 6181746709121420439L;

    private PreferenzaEspressa preferenzaEspressa;

    public PreferenzaEspressaParsedIncomingMessage () {
    }

    public PreferenzaEspressa getPreferenzaEspressa () {
        return preferenzaEspressa;
    }

    public void setPreferenzaEspressa ( PreferenzaEspressa preferenzaEspressa ) {
        this.preferenzaEspressa = preferenzaEspressa;
    }



}
