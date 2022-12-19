/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.util.exception;

public class GenericParameterException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String message;

	public GenericParameterException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}