/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.integration.domain;

/**
 * Entity utile a centralizzare i metodi comuni delle entities.
 * 
 * @param <ID> Primary key type
 */
public abstract class AbstractEntity<ID> {

    /**
     * Serve a centralizzare i metodi equals per tutte le entities.
     * 
     * @return primary key.
     */
    public abstract ID getId ();

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = ( prime * result ) + ( ( getId () == null ) ? 0 : getId ().hashCode () );
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals ( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass () != obj.getClass () ) {
            return false;
        }
        AbstractEntity<?> other = (AbstractEntity<?>) obj;
        if ( getId () == null ) {
            if ( other.getId () != null ) {
                return false;
            }
        } else if ( !getId ().equals ( other.getId () ) ) {
            return false;
        }
        return true;
    }

}
