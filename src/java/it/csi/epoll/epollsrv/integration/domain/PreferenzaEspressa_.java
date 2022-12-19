/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-14T15:58:29.230+0200")
@StaticMetamodel(PreferenzaEspressa.class)
public class PreferenzaEspressa_ {
	public static volatile SingularAttribute<PreferenzaEspressa, String> uuid;
	public static volatile SingularAttribute<PreferenzaEspressa, Integer> conteggio;
	public static volatile SingularAttribute<PreferenzaEspressa, String> preferenza;
	public static volatile SingularAttribute<PreferenzaEspressa, Boolean> valido;
	public static volatile SingularAttribute<PreferenzaEspressa, Poll> poll;
}
