/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-14T15:58:29.223+0200")
@StaticMetamodel(Preferenza.class)
public class Preferenza_ {
	public static volatile SingularAttribute<Preferenza, Long> id;
	public static volatile SingularAttribute<Preferenza, String> preferenza;
	public static volatile SingularAttribute<Preferenza, Poll> poll;
	public static volatile SingularAttribute<Preferenza, String> codPreferenza;
}
