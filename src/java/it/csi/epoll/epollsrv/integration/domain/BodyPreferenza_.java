/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-02-09T18:53:57.915+0100")
@StaticMetamodel(BodyPreferenza.class)
public class BodyPreferenza_ {

    public static volatile SingularAttribute<BodyPreferenza, String> uuid;
	public static volatile SingularAttribute<BodyPreferenza, String> bodyPreferenza;
	public static volatile SingularAttribute<BodyPreferenza, Poll> poll;
}
