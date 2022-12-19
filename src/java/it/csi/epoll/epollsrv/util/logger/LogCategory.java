/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.util.logger;

import static it.csi.epoll.epollsrv.util.logger.LoggerConstants.EPOLLSRV_ROOT_LOG_CATEGORY;


public enum LogCategory {

        EPOLLSRV_ROOT_LOG_CATEGORY_BUSINESS_LOG_CATEGORY ( EPOLLSRV_ROOT_LOG_CATEGORY.getValue () + ".business" ),
        EPOLLSRV_ROOT_LOG_CATEGORY_FILTER_LOG_CATEGORY ( EPOLLSRV_ROOT_LOG_CATEGORY.getValue () + ".filter" ),
        EPOLLSRV_ROOT_LOG_CATEGORY_DTO_LOG_CATEGORY ( EPOLLSRV_ROOT_LOG_CATEGORY.getValue () + ".dto" ),
        EPOLLSRV_ROOT_LOG_CATEGORY_UTIL_LOG_CATEGORY ( EPOLLSRV_ROOT_LOG_CATEGORY.getValue () + ".util" ),
        EPOLLSRV_ROOT_LOG_CATEGORY_SCHEDULER_LOG_CATEGORY ( EPOLLSRV_ROOT_LOG_CATEGORY.getValue () + ".scheduler" );

    private String category;

    private LogCategory ( String category ) {
        this.category = category;
    }

    public String getCategory () {
        return category;
    }
}
