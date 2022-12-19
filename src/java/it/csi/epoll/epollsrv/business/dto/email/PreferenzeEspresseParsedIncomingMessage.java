/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.business.dto.email;

import java.util.List;

import it.csi.epoll.epollsrv.integration.domain.PreferenzaEspressa;

public class PreferenzeEspresseParsedIncomingMessage extends ParsedIncomingMessage{

	private static final long serialVersionUID = -5618352548061778959L;
	
	private List<PreferenzaEspressa> preferenzeEspresse;

	public PreferenzeEspresseParsedIncomingMessage() {
	}

	public List<PreferenzaEspressa> getPreferenzeEspresse() {
		return preferenzeEspresse;
	}

	public void setPreferenzeEspresse(List<PreferenzaEspressa> preferenzeEspresse) {
		this.preferenzeEspresse = preferenzeEspresse;
	}
	
}
