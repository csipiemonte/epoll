/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-14T15:58:29.209+0200")
@StaticMetamodel(PecCfg.class)
public class PecCfg_ {
	public static volatile SingularAttribute<PecCfg, Long> id;
	public static volatile SingularAttribute<PecCfg, String> imapInboxFolder;
	public static volatile SingularAttribute<PecCfg, byte[]> imapPassword;
	public static volatile SingularAttribute<PecCfg, String> imapServerHostname;
	public static volatile SingularAttribute<PecCfg, Integer> imapServerPort;
	public static volatile SingularAttribute<PecCfg, String> imapUsername;
	public static volatile SingularAttribute<PecCfg, String> indirizzoPec;
	public static volatile SingularAttribute<PecCfg, byte[]> smtpPassword;
	public static volatile SingularAttribute<PecCfg, String> smtpServerHostname;
	public static volatile SingularAttribute<PecCfg, Integer> smtpServerPort;
	public static volatile SingularAttribute<PecCfg, String> smtpUsername;
	public static volatile SingularAttribute<PecCfg, Ente> ente;
}
