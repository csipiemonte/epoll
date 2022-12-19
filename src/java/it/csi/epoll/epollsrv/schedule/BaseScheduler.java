/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.schedule;


/**
 * @author lfantini Interfaccia comune a tutti gli scheduler. Ogni scheduler deve essere composto da interfaccia e implementazione. L'interfaccia estendera'
 *         BaseScheduler
 */

public interface BaseScheduler {

    public void execute ();

}
