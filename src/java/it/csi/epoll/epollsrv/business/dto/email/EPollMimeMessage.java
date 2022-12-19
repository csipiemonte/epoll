/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import javax.mail.Session;

import com.sun.mail.smtp.SMTPMessage;// verificare di sostituirlo con javax.mail.internet.MimeMessage


/**
 * Oggetto che rappresenta la mail da inviare.
 */
public class EPollMimeMessage extends SMTPMessage {

    public EPollMimeMessage ( Session session ) {
        super ( session );
    }

}
