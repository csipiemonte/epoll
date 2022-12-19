/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.template;

import java.util.HashMap;
import java.util.Map;

import it.csi.epoll.epollsrv.business.dto.RisultatiScrutinioPollDTO;


/**
 *
 */

public class ScrutinioVotazioneTemplate extends AbstractEmailTemplate {

    private static final String NOME_MODELLO_POLL = "Votazione";

    private static final String NOME_PREFERENZE_VALIDE = "PreferenzeValide";

    private static final String NOME_PREFERENZE_NON_VALIDE = "PreferenzeNonValide";
    
    private static final String NOME_SCHEDE_BIANCHE = "SchedeBianche";

    private static final String NOME_VOTANTI = "votanti";

    private static final String NOME_ELETTORI = "elettori";

    private RisultatiScrutinioPollDTO risultatiScrutinio;

    public ScrutinioVotazioneTemplate () {
        super ( "email_scrutinio_votazione.ftl" );
    }

    public ScrutinioVotazioneTemplate ( RisultatiScrutinioPollDTO risultatiScrutinio ) {
        super ( "email_scrutinio_votazione.ftl" );

        this.risultatiScrutinio = risultatiScrutinio;
    }

    @Override
    public Map<String, Object> creaModello () {
        if ( risultatiScrutinio == null ) {
            return null;
        }

        Map<String, Object> modello = new HashMap<> ();
        modello.put ( NOME_MODELLO_POLL, risultatiScrutinio.getPollDTO () );
        modello.put ( NOME_PREFERENZE_VALIDE, risultatiScrutinio.getPreferenzeValide () );
        modello.put ( NOME_PREFERENZE_NON_VALIDE, risultatiScrutinio.getPreferenzeNonValide () );
        modello.put ( NOME_SCHEDE_BIANCHE, risultatiScrutinio.getSchedeBianche () );
        modello.put ( NOME_VOTANTI, risultatiScrutinio.getElettori () );
        modello.put ( NOME_ELETTORI, risultatiScrutinio.getElettoriTotali () );
        return modello;
    }

    public RisultatiScrutinioPollDTO getRisultatiScrutinio () {
        return risultatiScrutinio;
    }

    public void setRisultatiScrutinio ( RisultatiScrutinioPollDTO risultatiScrutinio ) {
        this.risultatiScrutinio = risultatiScrutinio;
    }

}
