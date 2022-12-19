/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util;

/**
 *
 */

public enum ParametriApplicativo {
    
    NOME_AMBIENTE ( "nome.ambiente", ExposurePolicy.EXTERNAL ),
    FILE_ESTENSIONI_AMMESSE ( "file.estensioni.ammesse", ExposurePolicy.EXTERNAL ),
    // PAGINAZIONE
    OPZIONI_PAGINAZIONE ( "num.elementi.per.pagina", ExposurePolicy.EXTERNAL );

    private String codice;

    private String valoreDefault;

    private Boolean obbligatorio;

    private ExposurePolicy policyEsposizione = ExposurePolicy.PUBLIC;

    public String getValoreDefault () {
        return valoreDefault;
    }

    public String getCodice () {
        return codice;
    }

    public Boolean getObbligatorio () {
        return obbligatorio;
    }

    public ExposurePolicy getPolicyEsposizione () {
        return policyEsposizione;
    }

    private ParametriApplicativo ( String codice, ExposurePolicy exposurePolicy ) {
        this.codice = codice;
        obbligatorio = true;
        policyEsposizione = exposurePolicy;
    }

    public enum ExposurePolicy {
        /**
         * Valore di default: parametro non riservato, disponibile solo a backend
         */
        PUBLIC,

        /**
         * Valore disponibile solo a backend e considerato sensibile.
         * Non verra' mostrato in nessun log ne' esposto all'esterno dell'applicativo
         */
        SENSIBLE,

        /**
         * Valore disponibile ed esposto anche a frontend
         */
        EXTERNAL
    }
}
