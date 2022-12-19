/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.util.email;

public class EmailParserSectionDelimiter {

	private boolean required;
	private String setionDelimiter;

	public EmailParserSectionDelimiter() {
	}
	
	public EmailParserSectionDelimiter(String setionDelimiter, boolean required) {
		this.required = required;
		this.setionDelimiter = setionDelimiter;
	}
	
	public String getSetionDelimiter() {
		return setionDelimiter;
	}

	public void setSetionDelimiter(String setionDelimiter) {
		this.setionDelimiter = setionDelimiter;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
	
}
