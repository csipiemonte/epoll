/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the EPOLL_T_ELETTORE database table.
 * 
 */
@Entity
@Table ( name = "EPOLL_T_ELETTORE" )
@NamedQuery ( name = "Elettore.findAll", query = "SELECT e FROM Elettore e" )
public class Elettore extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator ( name = "EPOLL_T_ELETTORE_ID_GENERATOR", sequenceName = "EPOLL_T_ELETTORE_ID_SEQ", allocationSize = 1 )
    @GeneratedValue ( strategy = GenerationType.SEQUENCE, generator = "EPOLL_T_ELETTORE_ID_GENERATOR" )
    private Long id;

    /**
     * 
     */
    public Elettore () {
        super ();
    }

    @Column ( name = "COGNOME_NOME" )
    private String cognomeNome;

    @Temporal ( TemporalType.TIMESTAMP )
    @Column ( name = "DT_DATA_VOTAZIONE" )
    private Date dtDataVotazione;

    private String email;

    //bi-directional many-to-one association to Poll
    @ManyToOne
    @JoinColumn ( nullable = false, name = "POLL_ID" )
    private Poll poll;

    @Generated ( "SparkTools" )
    private Elettore ( Builder builder ) {
        this.id = builder.id;
        this.cognomeNome = builder.cognomeNome;
        this.dtDataVotazione = builder.dtDataVotazione;
        this.email = builder.email;
        this.poll = builder.poll;
    }

    @Override
    public Long getId () {
        return this.id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public String getCognomeNome () {
        return this.cognomeNome;
    }

    public void setCognomeNome ( String cognomeNome ) {
        this.cognomeNome = cognomeNome;
    }

    public Date getDtDataVotazione () {
        return this.dtDataVotazione;
    }

    public void setDtDataVotazione ( Date dtDataVotazione ) {
        this.dtDataVotazione = dtDataVotazione;
    }

    public String getEmail () {
        return this.email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public Poll getPoll () {
        return this.poll;
    }

    public void setPoll ( Poll poll ) {
        this.poll = poll;
    }

    /**
     * Creates builder to build {@link Elettore}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link Elettore}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private Long id;

        private String cognomeNome;

        private Date dtDataVotazione;

        private String email;

        private Poll poll;

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

        public Builder withDtDataVotazione ( Date dtDataVotazione ) {
            this.dtDataVotazione = dtDataVotazione;
            return this;
        }

        public Builder withEmail ( String email ) {
            this.email = email;
            return this;
        }

        public Builder withPoll ( Poll poll ) {
            this.poll = poll;
            return this;
        }

        public Elettore build () {
            return new Elettore ( this );
        }
    }

}
