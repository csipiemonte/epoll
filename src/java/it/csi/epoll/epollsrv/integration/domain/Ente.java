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

import org.hibernate.annotations.Immutable;


/**
 * The persistent class for the EPOLL_T_ENTE database table.
 * 
 */
@Immutable
@Entity
@Table(name="EPOLL_T_ENTE")
@NamedQuery(name="Ente.findAll", query="SELECT e FROM Ente e")
public class Ente extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator ( name = "EPOLL_T_ENTE_ID_GENERATOR", sequenceName = "EPOLL_T_ENTE_ID_SEQ", allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EPOLL_T_ENTE_ID_GENERATOR")
    private Long id;

	@Column(name="COD_FISCALE")
	private String codFiscale;

    //bi-directional many-to-one association to Poll
    @ManyToOne
    @JoinColumn ( nullable = false, name = "COD_ALGORITMO" )
    private AlgoritmoVotazione algoritmoVotazione;

	private String codice;

	private String descrizione;

	//bi-directional many-to-one association to PecCfg
	@OneToMany(mappedBy="ente")
	private List<PecCfg> pecCfgList;

	//bi-directional many-to-one association to Poll
	@OneToMany(mappedBy="ente")
	private List<Poll> pollList;

	//bi-directional many-to-one association to Presidente
	@OneToMany(mappedBy="ente")
	private List<Presidente> presidenteList;

    @Generated ( "SparkTools" )
    private Ente ( Builder builder ) {
        this.id = builder.id;
        this.codFiscale = builder.codFiscale;
        this.algoritmoVotazione = builder.algoritmoVotazione;
        this.codice = builder.codice;
        this.descrizione = builder.descrizione;
        this.pecCfgList = builder.pecCfgList;
        this.pollList = builder.pollList;
        this.presidenteList = builder.presidenteList;
    }

    /**
     * 
     */
    public Ente () {
        super ();
    }

    @Override
    public Long getId () {
		return this.id;
	}

    public void setId ( Long id ) {
		this.id = id;
	}

	public String getCodFiscale() {
		return this.codFiscale;
	}

	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<PecCfg> getPecCfgList() {
		return this.pecCfgList;
	}

	public void setPecCfgList(List<PecCfg> pecCfgList) {
		this.pecCfgList = pecCfgList;
	}

	public PecCfg addPecCfgList(PecCfg pecCfgList) {
		getPecCfgList().add(pecCfgList);
		pecCfgList.setEnte(this);

		return pecCfgList;
	}

	public PecCfg removePecCfgList(PecCfg pecCfgList) {
		getPecCfgList().remove(pecCfgList);
		pecCfgList.setEnte(null);

		return pecCfgList;
	}

	public List<Poll> getPollList() {
		return this.pollList;
	}

	public void setPollList(List<Poll> pollList) {
		this.pollList = pollList;
	}

	public Poll addPollList(Poll pollList) {
		getPollList().add(pollList);
		pollList.setEnte(this);

		return pollList;
	}

	public Poll removePollList(Poll pollList) {
		getPollList().remove(pollList);
		pollList.setEnte(null);

		return pollList;
	}

	public List<Presidente> getPresidenteList() {
		return this.presidenteList;
	}

	public void setPresidenteList(List<Presidente> presidenteList) {
		this.presidenteList = presidenteList;
	}

	public Presidente addPresidenteList(Presidente presidenteList) {
		getPresidenteList().add(presidenteList);
		presidenteList.setEnte(this);

		return presidenteList;
	}

	public Presidente removePresidenteList(Presidente presidenteList) {
		getPresidenteList().remove(presidenteList);
		presidenteList.setEnte(null);

		return presidenteList;
	}


    /**
     * @return the algoritmoVotazione
     */
    public AlgoritmoVotazione getAlgoritmoVotazione () {
        return algoritmoVotazione;
    }


    /**
     * @param algoritmoVotazione the algoritmoVotazione to set
     */
    public void setAlgoritmoVotazione ( AlgoritmoVotazione algoritmoVotazione ) {
        this.algoritmoVotazione = algoritmoVotazione;
    }

    /**
     * Creates builder to build {@link Ente}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link Ente}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private Long id;

        private String codFiscale;

        private AlgoritmoVotazione algoritmoVotazione;

        private String codice;

        private String descrizione;

        private List<PecCfg> pecCfgList = Collections.emptyList ();

        private List<Poll> pollList = Collections.emptyList ();

        private List<Presidente> presidenteList = Collections.emptyList ();

        private Builder () {
        }

        public Builder withId ( Long id ) {
            this.id = id;
            return this;
        }

        public Builder withCodFiscale ( String codFiscale ) {
            this.codFiscale = codFiscale;
            return this;
        }

        public Builder withAlgoritmoVotazione ( AlgoritmoVotazione algoritmoVotazione ) {
            this.algoritmoVotazione = algoritmoVotazione;
            return this;
        }

        public Builder withCodice ( String codice ) {
            this.codice = codice;
            return this;
        }

        public Builder withDescrizione ( String descrizione ) {
            this.descrizione = descrizione;
            return this;
        }

        public Builder withPecCfgList ( List<PecCfg> pecCfgList ) {
            this.pecCfgList = pecCfgList;
            return this;
        }

        public Builder withPollList ( List<Poll> pollList ) {
            this.pollList = pollList;
            return this;
        }

        public Builder withPresidenteList ( List<Presidente> presidenteList ) {
            this.presidenteList = presidenteList;
            return this;
        }

        public Ente build () {
            return new Ente ( this );
        }
    }

}
