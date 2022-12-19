/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.util.exception;

public class UnexpectedSectionException extends Exception {

	private static final long serialVersionUID = 1L;
	
    private final String section;
	
	public UnexpectedSectionException(String section) {
		this.section = section;
	}

	public String getSection() {
		return section;
	}

}
