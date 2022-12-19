/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.test.epoll.pec;

import java.io.InputStream;

/**
 * <p>MailAttachmentUtil class.</p>
 *
 * @author mirco
 * @version $Id: $Id
 */
public class MailAttachmentUtil {
	/**
	 * 
	 */
	private String contentType;
	private InputStream stream;
	private String fileName;
	private int size;

	/**
	 * <p>Constructor for MailAttachmentUtil.</p>
	 */
	public MailAttachmentUtil() {
		super();
	}

	/**
	 * <p>Constructor for MailAttachmentUtil.</p>
	 *
	 * @param mimeType a {@link java.lang.String} object.
	 * @param stream a {@link java.io.InputStream} object.
	 * @param fileName a {@link java.lang.String} object.
	 * @param size a int.
	 */
	public MailAttachmentUtil(String mimeType, InputStream stream, String fileName, int size) {
		super();
		this.contentType = mimeType;
		this.stream = stream;
		this.fileName = fileName;
		this.size = size;
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

	/**
	 * <p>Getter for the field <code>stream</code>.</p>
	 *
	 * @return a {@link java.io.InputStream} object.
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * <p>Setter for the field <code>stream</code>.</p>
	 *
	 * @param stream a {@link java.io.InputStream} object.
	 */
	public void setStream(InputStream stream) {
		this.stream = stream;
	}

	/**
	 * <p>Getter for the field <code>fileName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * <p>Setter for the field <code>fileName</code>.</p>
	 *
	 * @param fileName a {@link java.lang.String} object.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * <p>Getter for the field <code>size</code>.</p>
	 *
	 * @return a int.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <p>Setter for the field <code>size</code>.</p>
	 *
	 * @param size a int.
	 */
	public void setSize(int size) {
		this.size = size;
	}

}
