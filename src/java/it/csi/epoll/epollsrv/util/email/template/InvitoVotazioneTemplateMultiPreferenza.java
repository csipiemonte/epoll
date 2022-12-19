/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.util.email.template;

import java.util.HashMap;
import java.util.Map;

import it.csi.epoll.epollsrv.business.dto.PollDTO;

public class InvitoVotazioneTemplateMultiPreferenza extends AbstractEmailTemplate{
	
	private static final String nomeModello = "Votazione";

    private PollDTO pollDTO;
	
	public InvitoVotazioneTemplateMultiPreferenza () {
        super ( "email_invito_votazione_multi_preferenza.ftl" );
    }

	@Override
	public Map<String, Object> creaModello() {
		if ( pollDTO == null )
            return null;

        Map<String, Object> modello = new HashMap<> ();
        modello.put ( nomeModello, pollDTO );
        return modello;
	}

	public PollDTO getPollDTO() {
		return pollDTO;
	}

	public void setPollDTO(PollDTO pollDTO) {
		this.pollDTO = pollDTO;
	}
	
}
