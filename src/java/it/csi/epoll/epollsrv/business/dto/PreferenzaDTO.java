/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;


/**
 *
 */

public class PreferenzaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String codPreferenza;
    
    private String preferenza;
    
    public String getCodPreferenza() {
		return codPreferenza;
	}

	public void setCodPreferenza(String codPreferenza) {
		this.codPreferenza = codPreferenza;
	}

	public String getPreferenza () {
        return preferenza;
    }

    public void setPreferenza ( String preferenza ) {
        this.preferenza = preferenza;
    }

    @Override
    public String toString () {
        return "PreferenzaDTO [codPreferenza=" + codPreferenza + ", preferenza=" + preferenza + "]";
    }

}
