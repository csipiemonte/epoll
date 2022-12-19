/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;

public class PreferenzaDaValidareDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String preferenza;
	
	private boolean valida;
	
	private boolean dataOraValida;
	
	public PreferenzaDaValidareDTO() {	
	}

	public String getPreferenza() {
		return preferenza;
	}

	public void setPreferenza(String preferenza) {
		this.preferenza = preferenza;
	}

	public boolean isValida() {
		return valida;
	}

	public void setValida(boolean valida) {
		this.valida = valida;
	}

	public boolean isDataOraValida() {
		return dataOraValida;
	}

	public void setDataOraValida(boolean dataOraValida) {
		this.dataOraValida = dataOraValida;
	}
	
}
