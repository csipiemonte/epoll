/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-14T15:58:29.243+0200")
@StaticMetamodel(Presidente.class)
public class Presidente_ {
	public static volatile SingularAttribute<Presidente, Long> id;
	public static volatile SingularAttribute<Presidente, String> cognomeNome;
	public static volatile SingularAttribute<Presidente, String> email;
	public static volatile ListAttribute<Presidente, Poll> pollList;
	public static volatile SingularAttribute<Presidente, Ente> ente;
}
