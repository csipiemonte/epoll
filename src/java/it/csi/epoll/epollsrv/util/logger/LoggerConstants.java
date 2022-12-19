/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.logger;

public enum LoggerConstants {

        EPOLLSRV_ROOT_LOG_CATEGORY ( "epoll.epollsrv" ),
        DATE_TIME_PATTERN ( "yyyy-MM-dd HH:mm:ss.SSS" ),
        DATE_PATTERN ( "yyyy-MM-dd" ),
        TIME_PATTERN ( "HH:mm:ss.SSS" ),
        DEFAULT_LOG ( "[%s::%s] %s" ),
        BEGIN ( "BEGIN." ),
        END ( "END." );

    private String value;

    /**
     * @param arg0
     * @param arg1
     */
    private LoggerConstants ( String value ) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue () {
        return value;
    }

}
