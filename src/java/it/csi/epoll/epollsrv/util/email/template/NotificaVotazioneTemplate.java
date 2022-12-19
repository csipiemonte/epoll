/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.template;

import java.util.HashMap;
import java.util.Map;

import it.csi.epoll.epollsrv.business.dto.RisultatiNotifichePollDTO;


/**
 *
 */

public class NotificaVotazioneTemplate extends AbstractEmailTemplate {

    private static final String NOME_MODELLO_POLL = "Votazione";

    private static final String NOME_VOTANTI = "votanti";

    private static final String NOME_ELETTORI = "elettori";

    private RisultatiNotifichePollDTO notifiche;

    public NotificaVotazioneTemplate ( RisultatiNotifichePollDTO notifiche ) {
        super ( "email_notifica_stato_poll.ftl" );

        this.notifiche = notifiche;
    }

    @Override
    public Map<String, Object> creaModello () {
        Map<String, Object> modello = new HashMap<> ();
        modello.put ( NOME_MODELLO_POLL, notifiche.getPollDTO () );
        modello.put ( NOME_VOTANTI, notifiche.getElettori () );
        modello.put ( NOME_ELETTORI, notifiche.getElettoriTotali () );
        return modello;
    }

    public RisultatiNotifichePollDTO getNotifiche () {
        return notifiche;
    }

    public void setNotifiche ( RisultatiNotifichePollDTO notifiche ) {
        this.notifiche = notifiche;
    }

}
