/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;


/**
 *
 */
public class AllegatoDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6222530549187579286L;

    private byte [] allegato;

    private String nomeAllegato;
    
    private String pathAllegato;
    
    private String tipoMIME;

    private boolean firmaPEC;

    private boolean certificazionePEC;

    public byte [] getAllegato () {
        return allegato;
    }

    public void setAllegato ( byte [] allegato ) {
        this.allegato = allegato;
    }

    public String getNomeAllegato () {
        return nomeAllegato;
    }

    public void setNomeAllegato ( String nomeAllegato ) {
        this.nomeAllegato = nomeAllegato;
    }
    
    public String getPathAllegato() {
		return pathAllegato;
	}

	public void setPathAllegato(String pathAllegato) {
		this.pathAllegato = pathAllegato;
	}

	public String getTipoMIME () {
        return tipoMIME;
    }

    public void setTipoMIME ( String tipoMIME ) {
        this.tipoMIME = tipoMIME;
    }

    public boolean isFirmaPEC () {
        return firmaPEC;
    }

    public void setFirmaPEC ( boolean firmaPEC ) {
        this.firmaPEC = firmaPEC;
    }

    public boolean isCertificazionePEC () {
        return certificazionePEC;
    }

    public void setCertificazionePEC ( boolean certificazionePEC ) {
        this.certificazionePEC = certificazionePEC;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString () {
        return String.format ( "AllegatoDTO [nomeAllegato=%s, allegato=%s, tipoMIME=%s, firmaPEC=%s, certificazionePEC=%s]", nomeAllegato,
            null != allegato ? new String ( allegato,
                StandardCharsets.UTF_8 )
                            : null,
            tipoMIME,
            firmaPEC,
            certificazionePEC );
    }

}
