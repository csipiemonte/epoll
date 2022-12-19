/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-14T15:58:29.125+0200")
@StaticMetamodel(Elettore.class)
public class Elettore_ {
	public static volatile SingularAttribute<Elettore, Long> id;
	public static volatile SingularAttribute<Elettore, String> cognomeNome;
	public static volatile SingularAttribute<Elettore, Date> dtDataVotazione;
	public static volatile SingularAttribute<Elettore, String> email;
	public static volatile SingularAttribute<Elettore, Poll> poll;
}
