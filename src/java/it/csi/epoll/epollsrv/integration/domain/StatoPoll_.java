/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-09T21:14:04.302+0200")
@StaticMetamodel(StatoPoll.class)
public class StatoPoll_ {
	public static volatile SingularAttribute<StatoPoll, String> codStato;
	public static volatile SingularAttribute<StatoPoll, String> descrizione;
	public static volatile ListAttribute<StatoPoll, Poll> pollList;
}
