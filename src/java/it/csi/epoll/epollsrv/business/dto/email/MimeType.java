/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;


/**
 *
 */

/**
 *
 */
public enum MimeType {

        TEXT_PLAIN ( "text/plain" ),
        TEXT_HTML ( "text/html" );
    
    /**
     * @param name
     * @param ordinal
     */
    private MimeType ( String value ) {
        this.value = value;
    }

    String value;

    
    /**
     * @return the value
     */
    public String getValue () {
        return value;
    }
    
}
