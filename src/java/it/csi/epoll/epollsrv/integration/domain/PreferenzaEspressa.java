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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the EPOLL_T_PREFERENZA_ESPRESSA database table.
 * 
 */
@Entity
@Table(name="EPOLL_T_PREFERENZA_ESPRESSA")
@NamedQuery(name="PreferenzaEspressa.findAll", query="SELECT p FROM PreferenzaEspressa p")
public class PreferenzaEspressa extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @Column ( nullable = false, name = "uuid" )
	private String uuid;

    private int conteggio;

	private String preferenza;

	private boolean valido;

	//bi-directional many-to-one association to Poll
	@ManyToOne
    @JoinColumn ( nullable = false, name = "POLL_ID" )
	private Poll poll;

    @Generated ( "SparkTools" )
    private PreferenzaEspressa ( Builder builder ) {
        this.uuid = builder.uuid;
        this.conteggio = builder.conteggio;
        this.preferenza = builder.preferenza;
        this.valido = builder.valido;
        this.poll = builder.poll;
    }

    /**
     * 
     */
    public PreferenzaEspressa () {
        super ();
    }

    @Override
    public String getId () {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


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
		return this.preferenza;
	}

	public void setPreferenza(String preferenza) {
		this.preferenza = preferenza;
	}

	public boolean getValido() {
		return this.valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public Poll getPoll() {
		return this.poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

    /**
     * Creates builder to build {@link PreferenzaEspressa}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link PreferenzaEspressa}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private String uuid;

        private int conteggio;

        private String preferenza;

        private boolean valido;

        private Poll poll;

        private Builder () {
        }

        public Builder withUuid ( String uuid ) {
            this.uuid = uuid;
            return this;
        }

        public Builder withConteggio ( int conteggio ) {
            this.conteggio = conteggio;
            return this;
        }

        public Builder withPreferenza ( String preferenza ) {
            this.preferenza = preferenza;
            return this;
        }

        public Builder withValido ( boolean valido ) {
            this.valido = valido;
            return this;
        }

        public Builder withPoll ( Poll poll ) {
            this.poll = poll;
            return this;
        }

        public PreferenzaEspressa build () {
            return new PreferenzaEspressa ( this );
        }
    }

}
