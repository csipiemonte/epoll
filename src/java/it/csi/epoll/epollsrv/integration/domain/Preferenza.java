/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the EPOLL_T_PREFERENZA database table.
 * 
 */
@Entity
@Table(name="EPOLL_T_PREFERENZA")
@NamedQuery(name="Preferenza.findAll", query="SELECT p FROM Preferenza p")
public class Preferenza extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator ( name = "EPOLL_T_PREFERENZA_ID_GENERATOR", sequenceName = "EPOLL_T_PREFERENZA_ID_SEQ", allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EPOLL_T_PREFERENZA_ID_GENERATOR")
    private Long id;
	
	@Column(name="preferenza")
	private String preferenza;
	
	@Column(name="cod_preferenza")
	private String codPreferenza;

	//bi-directional many-to-one association to Poll
	@ManyToOne
    @JoinColumn ( nullable = false, name = "POLL_ID" )
	private Poll poll;

    @Generated ( "SparkTools" )
    private Preferenza ( Builder builder ) {
        this.id = builder.id;
        this.preferenza = builder.preferenza;
        this.poll = builder.poll;
        this.codPreferenza = builder.codPreferenza;
    }

    /**
     * 
     */
    public Preferenza () {
        super ();
    }

    @Override
    public Long getId () {
		return this.id;
	}

    public void setId ( Long id ) {
		this.id = id;
	}

	public String getPreferenza() {
		return this.preferenza;
	}

	public void setPreferenza(String preferenza) {
		this.preferenza = preferenza;
	}
	
	public String getCodPreferenza() {
		return codPreferenza;
	}

	public void setCodPreferenza(String codPreferenza) {
		this.codPreferenza = codPreferenza;
	}

	public Poll getPoll() {
		return this.poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

    /**
     * Creates builder to build {@link Preferenza}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link Preferenza}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private Long id;

        private String preferenza;
        
        private String codPreferenza;
        
        private Poll poll;

        private Builder () {
        }

        public Builder withId ( Long id ) {
            this.id = id;
            return this;
        }

        public Builder withPreferenza ( String preferenza ) {
            this.preferenza = preferenza;
            return this;
        }
        
        public Builder withCodPreferenza ( String codPreferenza ) {
            this.codPreferenza = codPreferenza;
            return this;
        }

        
        public Builder withPoll ( Poll poll ) {
            this.poll = poll;
            return this;
        }

        public Preferenza build () {
            return new Preferenza ( this );
        }
    }

}
