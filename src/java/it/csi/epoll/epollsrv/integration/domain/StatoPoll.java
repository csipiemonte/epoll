/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;


/**
 * The persistent class for the EPOLL_D_STATO_POLL database table.
 * 
 */
@Immutable
@Entity
@Table(name="EPOLL_D_STATO_POLL")
@NamedQuery(name="StatoPoll.findAll", query="SELECT s FROM StatoPoll s")
public class StatoPoll extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<String> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_STATO")
    private String codStato;

	private String descrizione;

	//bi-directional many-to-one association to Poll
    @OneToMany ( mappedBy = "statoPoll" )
    private List<Poll> pollList;

    @Generated ( "SparkTools" )
    private StatoPoll ( Builder builder ) {
        this.codStato = builder.codStato;
        this.descrizione = builder.descrizione;
        this.pollList = builder.pollList;
    }

    /**
     * 
     */
	public StatoPoll() {
        super ();
	}

    @Override
    public String getId () {
		return this.codStato;
	}

    public void setId ( String codStato ) {
		this.codStato = codStato;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Poll> getPollList() {
        return this.pollList;
	}

    public void setPollList ( List<Poll> pollList ) {
        this.pollList = pollList;
	}

    public Poll addPollList ( Poll pollList ) {
        getPollList ().add ( pollList );
        pollList.setStatoPoll ( this );

        return pollList;
	}

    public Poll removePollList ( Poll pollList ) {
        getPollList ().remove ( pollList );
        pollList.setStatoPoll ( null );

        return pollList;
	}

    /**
     * Creates builder to build {@link StatoPoll}.
     * 
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link StatoPoll}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private String codStato;

        private String descrizione;

        private List<Poll> pollList = Collections.emptyList ();

        private Builder () {
        }

        public Builder withCodStato ( String codStato ) {
            this.codStato = codStato;
            return this;
        }

        public Builder withDescrizione ( String descrizione ) {
            this.descrizione = descrizione;
            return this;
        }

        public Builder withPollList ( List<Poll> pollList ) {
            this.pollList = pollList;
            return this;
        }

        public StatoPoll build () {
            return new StatoPoll ( this );
        }
    }

}
