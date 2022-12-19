/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-02-09T18:53:57.929+0100")
@StaticMetamodel(Poll.class)
public class Poll_ {
	public static volatile SingularAttribute<Poll, Long> id;
	public static volatile SingularAttribute<Poll, String> codice;
	public static volatile SingularAttribute<Poll, Date> dtCreazione;
	public static volatile SingularAttribute<Poll, Date> dtFineValidita;
	public static volatile SingularAttribute<Poll, Date> dtInizioValidita;
	public static volatile SingularAttribute<Poll, String> oggetto;
	public static volatile SingularAttribute<Poll, String> testo;
	public static volatile ListAttribute<Poll, Elettore> elettoreList;
	public static volatile SingularAttribute<Poll, Ente> ente;
	public static volatile SingularAttribute<Poll, Presidente> presidente;
	public static volatile ListAttribute<Poll, Preferenza> preferenzaList;
	public static volatile ListAttribute<Poll, PreferenzaEspressa> preferenzaEspressaList;
	public static volatile SingularAttribute<Poll, StatoPoll> statoPoll;
	public static volatile ListAttribute<Poll, BodyPreferenza> bodyPreferenzaList;
	public static volatile SingularAttribute<Poll, Integer> preferenzeDaEsprimere;
}
