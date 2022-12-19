/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the epoll_d_algoritmo_votazione database table.
 * 
 */
@Entity
@Table ( name = "epoll_d_algoritmo_votazione" )
@NamedQuery ( name = "AlgoritmoVotazione.findAll", query = "SELECT e FROM AlgoritmoVotazione e" )
public class AlgoritmoVotazione extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column ( nullable = false, name = "cod_algoritmo" )
    private String codAlgoritmo;

    private String descrizione;

    @Generated ( "SparkTools" )
    private AlgoritmoVotazione ( Builder builder ) {
        this.codAlgoritmo = builder.codAlgoritmo;
        this.descrizione = builder.descrizione;
    }
    
    public AlgoritmoVotazione() {
    	
    }

    public String getCodAlgoritmo () {
        return this.codAlgoritmo;
    }

    public void setCodAlgoritmo ( String codAlgoritmo ) {
        this.codAlgoritmo = codAlgoritmo;
    }

    public String getDescrizione () {
        return this.descrizione;
    }

    public void setDescrizione ( String descrizione ) {
        this.descrizione = descrizione;
    }

    @Override
    public String getId () {
        return this.codAlgoritmo;
    }

    /**
     * Creates builder to build {@link AlgoritmoVotazione}.
     * 
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link AlgoritmoVotazione}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private String codAlgoritmo;

        private String descrizione;

        private Builder () {
        }

        public Builder withCodAlgoritmo ( String codAlgoritmo ) {
            this.codAlgoritmo = codAlgoritmo;
            return this;
        }

        public Builder withDescrizione ( String descrizione ) {
            this.descrizione = descrizione;
            return this;
        }

        public AlgoritmoVotazione build () {
            return new AlgoritmoVotazione ( this );
        }
    }

}
