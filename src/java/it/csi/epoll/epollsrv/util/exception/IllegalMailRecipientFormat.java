/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.util.exception;

public class IllegalMailRecipientFormat extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String recipient;
	
	public IllegalMailRecipientFormat(String recipient) {
		this.recipient = recipient;
	}

	public String getRecipient() {
		return recipient;
	}

}
