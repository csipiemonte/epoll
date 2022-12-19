/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.schedule;


/**
 * @author lfantini
 * Scheduler che legge tutte le mail nuove e agisce di conseguenza:
 *
 * * creando un nuovo poll
 * * effettuando le operazioni di voto
 * * rispondendo al mittente nel caso in cui la mail non rispetti il formato desiderato
 */

public interface EPollNewMailScheduler extends BaseScheduler {

    public void test ();

}
