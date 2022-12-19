/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;


/**
 *
 */

public class PreferenzaEspressaDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int conteggio;

    private String preferenza;

    private boolean valido;

    /**
     * @return the conteggio
     */
    public int getConteggio () {
        return conteggio;
    }

    /**
     * @param conteggio the conteggio to set
     */
    public void setConteggio ( int conteggio ) {
        this.conteggio = conteggio;
    }

    public String getPreferenza () {
        return preferenza;
    }

    public void setPreferenza ( String preferenza ) {
        this.preferenza = preferenza;
    }

    public boolean isValido () {
        return valido;
    }

    public void setValido ( boolean valido ) {
        this.valido = valido;
    }

    @Override
    public String toString () {
        return "PreferenzaEspressaDTO [conteggio=" + conteggio +
            ", preferenza=" + preferenza
            + ", valido=" + valido
            + "]";
    }

}
