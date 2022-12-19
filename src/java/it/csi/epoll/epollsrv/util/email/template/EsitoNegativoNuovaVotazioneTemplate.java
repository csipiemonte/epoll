/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.template;

import java.util.HashMap;
import java.util.Map;

import it.csi.epoll.epollsrv.business.dto.PollDTO;


/**
 *
 */

public class EsitoNegativoNuovaVotazioneTemplate extends AbstractEmailTemplate {

    private static final String nomeModelloPoll = "Votazione";
    private static final String nomeModelloMotivazione = "Motivazione";

    private PollDTO poll;
    private String motivazione;

    public EsitoNegativoNuovaVotazioneTemplate () {
        super ( "email_esito_negativo_nuova_votazione.ftl" );
    }
    
    public EsitoNegativoNuovaVotazioneTemplate (PollDTO poll, String motivazione) {
        super ( "email_esito_negativo_nuova_votazione.ftl" );
        
        this.poll = poll;
        this.motivazione = motivazione;
    }

    @Override
    public Map<String, Object> creaModello () {
        Map<String, Object> modello = new HashMap<> ();
        modello.put ( nomeModelloPoll, poll );
        modello.put ( nomeModelloMotivazione, motivazione );
        return modello;
    }

    public PollDTO getPollDTO () {
        return poll;
    }

    public void setPollDTO ( PollDTO pollDTO ) {
        this.poll = pollDTO;
    }
    
    public String getMotivazione () {
        return motivazione;
    }

    public void setMotivazione ( String motivazione ) {
        this.motivazione = motivazione;
    }

}
