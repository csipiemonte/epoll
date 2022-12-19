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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the EPOLL_T_PRESIDENTE database table.
 * 
 */
@Entity
@Table(name="EPOLL_T_PRESIDENTE")
@NamedQuery(name="Presidente.findAll", query="SELECT p FROM Presidente p")
public class Presidente extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator ( name = "EPOLL_T_PRESIDENTE_ID_GENERATOR", sequenceName = "EPOLL_T_PRESIDENTE_ID_SEQ", allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EPOLL_T_PRESIDENTE_ID_GENERATOR")
    private Long id;

	@Column(name="COGNOME_NOME")
	private String cognomeNome;

	private String email;

	//bi-directional many-to-one association to Poll
	@OneToMany(mappedBy="presidente")
	private List<Poll> pollList;

	//bi-directional many-to-one association to Ente
	@ManyToOne
	@JoinColumn(name="ID_ENTE")
	private Ente ente;

    @Generated ( "SparkTools" )
    private Presidente ( Builder builder ) {
        this.id = builder.id;
        this.cognomeNome = builder.cognomeNome;
        this.email = builder.email;
        this.pollList = builder.pollList;
        this.ente = builder.ente;
    }

    /**
     * 
     */
    public Presidente () {
        super ();
    }

    @Override
    public Long getId () {
		return this.id;
	}

    public void setId ( Long id ) {
		this.id = id;
	}

	public String getCognomeNome() {
		return this.cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Poll> getPollList() {
		return this.pollList;
	}

	public void setPollList(List<Poll> pollList) {
		this.pollList = pollList;
	}

	public Poll addPollList(Poll pollList) {
		getPollList().add(pollList);
		pollList.setPresidente(this);

		return pollList;
	}

	public Poll removePollList(Poll pollList) {
		getPollList().remove(pollList);
		pollList.setPresidente(null);

		return pollList;
	}

	public Ente getEnte() {
		return this.ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

    /**
     * Creates builder to build {@link Presidente}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link Presidente}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private Long id;

        private String cognomeNome;

        private String email;

        private List<Poll> pollList = Collections.emptyList ();

        private Ente ente;

        private Builder () {
        }

        public Builder withId ( Long id ) {
            this.id = id;
            return this;
        }

        public Builder withCognomeNome ( String cognomeNome ) {
            this.cognomeNome = cognomeNome;
            return this;
        }

        public Builder withEmail ( String email ) {
            this.email = email;
            return this;
        }

        public Builder withPollList ( List<Poll> pollList ) {
            this.pollList = pollList;
            return this;
        }

        public Builder withEnte ( Ente ente ) {
            this.ente = ente;
            return this;
        }

        public Presidente build () {
            return new Presidente ( this );
        }
    }

}
