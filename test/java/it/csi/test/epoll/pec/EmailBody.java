/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.test.epoll.pec;


/**
 * <p>EmailBody class.</p>
 *
 * @author mirco
 * @version $Id: $Id
 */
public class EmailBody {
	private String body;
	private boolean isHtml;
	private String contentType;

	/**
	 * <p>Constructor for EmailBody.</p>
	 *
	 * @param body a {@link java.lang.String} object.
	 * @param contentType a {@link java.lang.String} object.
	 */
	public EmailBody(String body, String contentType) {
		this.body = body;
		this.contentType = contentType;

		this.isHtml = false;
		if (contentType != null && contentType.indexOf(MailUtils.CONTENT_TYPE_TEXT_HTML) > -1) {
			this.isHtml = true;
		}
	}

	/**
	 * <p>Constructor for EmailBody.</p>
	 *
	 * @param body a {@link java.lang.String} object.
	 * @param isHtml a boolean.
	 */
	public EmailBody(String body, boolean isHtml) {
		this.body = body;
		this.isHtml = isHtml;
	}

	// public EmailBody(String body, boolean isHtml, String charset) {
	// this(body, isHtml);
	// this.charset = charset;
	// }

	/**
	 * <p>Getter for the field <code>body</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getBody() {
		return body;
	}

	/**
	 * <p>Setter for the field <code>body</code>.</p>
	 *
	 * @param body a {@link java.lang.String} object.
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * <p>isHtml.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isHtml() {
		return isHtml;
	}

	/**
	 * <p>setHtml.</p>
	 *
	 * @param isHtml a boolean.
	 */
	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	/**
	 * <p>Getter for the field <code>contentType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * <p>Setter for the field <code>contentType</code>.</p>
	 *
	 * @param contentType a {@link java.lang.String} object.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return body;
	}
}
