/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-02-09T18:53:57.919+0100")
@StaticMetamodel(Ente.class)
public class Ente_ {
	public static volatile SingularAttribute<Ente, Long> id;
	public static volatile SingularAttribute<Ente, String> codFiscale;
	public static volatile SingularAttribute<Ente, AlgoritmoVotazione> algoritmoVotazione;
	public static volatile SingularAttribute<Ente, String> codice;
	public static volatile SingularAttribute<Ente, String> descrizione;
	public static volatile ListAttribute<Ente, PecCfg> pecCfgList;
	public static volatile ListAttribute<Ente, Poll> pollList;
	public static volatile ListAttribute<Ente, Presidente> presidenteList;
}
