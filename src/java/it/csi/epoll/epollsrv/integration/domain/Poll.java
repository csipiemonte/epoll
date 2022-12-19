/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the EPOLL_T_POLL database table.
 * 
 */
@Entity
@Table(name="EPOLL_T_POLL")
@NamedQuery(name="Poll.findAll", query="SELECT p FROM Poll p")
public class Poll extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator ( name = "EPOLL_T_POLL_ID_GENERATOR", sequenceName = "EPOLL_T_POLL_ID_SEQ", allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EPOLL_T_POLL_ID_GENERATOR")
    private Long id;

	private String codice;

    @Temporal ( TemporalType.TIMESTAMP )
	@Column(name="DT_CREAZIONE")
    private Date dtCreazione;

    @Temporal ( TemporalType.TIMESTAMP )
	@Column(name="DT_FINE_VALIDITA")
    private Date dtFineValidita;

    @Temporal ( TemporalType.TIMESTAMP )
	@Column(name="DT_INIZIO_VALIDITA")
    private Date dtInizioValidita;

	private String oggetto;

	@Column(name="TESTO")
	private String testo;

	//bi-directional many-to-one association to Elettore
	@OneToMany(mappedBy="poll")
	private List<Elettore> elettoreList;

	//bi-directional many-to-one association to Ente
	@ManyToOne
	@JoinColumn(name="ID_ENTE")
	private Ente ente;

	//bi-directional many-to-one association to Presidente
	@ManyToOne
	@JoinColumn(name="ID_PRESIDENTE")
	private Presidente presidente;

	//bi-directional many-to-one association to Preferenza
	@OneToMany(mappedBy="poll")
	private List<Preferenza> preferenzaList;

	//bi-directional many-to-one association to PreferenzaEspressa
	@OneToMany(mappedBy="poll")
	private List<PreferenzaEspressa> preferenzaEspressaList;
	
    //bi-directional many-to-one association to StatoPoll
    @ManyToOne
    @JoinColumn ( name = "COD_STATO" )
    private StatoPoll statoPoll;

    //bi-directional many-to-one association to EpollTBodyPreferenza
    @OneToMany ( mappedBy = "poll" )
    private List<BodyPreferenza> bodyPreferenzaList;
    
    @Column(name="numero_max_preferenze")
    private Integer preferenzeDaEsprimere;

    @Temporal ( TemporalType.TIMESTAMP )
    @Column ( name = "DT_ULTIMA_NOTIFICA" )
    private Date dtUltimaNotifica;

    @Column ( name = "FREQUENZA_AGGIORNAMENTO" )
    private Long frequenzaAggiornamento;

    @Generated ( "SparkTools" )
    private Poll ( Builder builder ) {
        this.id = builder.id;
        this.codice = builder.codice;
        this.dtCreazione = builder.dtCreazione;
        this.dtFineValidita = builder.dtFineValidita;
        this.dtInizioValidita = builder.dtInizioValidita;
        this.oggetto = builder.oggetto;
        this.testo = builder.testo;
        this.elettoreList = builder.elettoreList;
        this.ente = builder.ente;
        this.presidente = builder.presidente;
        this.preferenzaList = builder.preferenzaList;
        this.preferenzaEspressaList = builder.preferenzaEspressaList;
        this.statoPoll = builder.statoPoll;
        this.bodyPreferenzaList = builder.bodyPreferenzaList;
        this.preferenzeDaEsprimere = builder.preferenzeDaEsprimere;
        this.dtUltimaNotifica = builder.dtUltimaNotifica;
        this.frequenzaAggiornamento = builder.frequenzaAggiornamento;
    }

    /**
     * 
     */
    public Poll () {
        super ();
    }

    @Override
    public Long getId () {
		return this.id;
	}

    public void setId ( Long id ) {
		this.id = id;
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

    public Date getDtCreazione () {
		return this.dtCreazione;
	}

    public void setDtCreazione ( Date dtCreazione ) {
		this.dtCreazione = dtCreazione;
	}

    public Date getDtFineValidita () {
		return this.dtFineValidita;
	}

    public void setDtFineValidita ( Date dtFineValidita ) {
		this.dtFineValidita = dtFineValidita;
	}

    public Date getDtInizioValidita () {
		return this.dtInizioValidita;
	}

    public void setDtInizioValidita ( Date dtInizioValidita ) {
		this.dtInizioValidita = dtInizioValidita;
	}

    public Date getDtUltimaNotifica () {
        return dtUltimaNotifica;
    }

    public void setDtUltimaNotifica ( Date dtUltimaNotifica ) {
        this.dtUltimaNotifica = dtUltimaNotifica;
    }

    public Long getFrequenzaAggiornamento () {
        return frequenzaAggiornamento;
    }

    public void setFrequenzaAggiornamento ( Long frequenzaAggiornamento ) {
        this.frequenzaAggiornamento = frequenzaAggiornamento;
    }

    public String getOggetto () {
		return this.oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public List<Elettore> getElettoreList() {
		return this.elettoreList;
	}

	public void setElettoreList(List<Elettore> elettoreList) {
		this.elettoreList = elettoreList;
	}

	public Elettore addElettoreList(Elettore elettoreList) {
		getElettoreList().add(elettoreList);
		elettoreList.setPoll(this);

		return elettoreList;
	}

	public Elettore removeElettoreList(Elettore elettoreList) {
		getElettoreList().remove(elettoreList);
		elettoreList.setPoll(null);

		return elettoreList;
	}

	public Ente getEnte() {
		return this.ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

	public Presidente getPresidente() {
		return this.presidente;
	}

	public void setPresidente(Presidente presidente) {
		this.presidente = presidente;
	}

	public List<Preferenza> getPreferenzaList() {
		return this.preferenzaList;
	}

	public void setPreferenzaList(List<Preferenza> preferenzaList) {
		this.preferenzaList = preferenzaList;
	}

	public Preferenza addPreferenzaList(Preferenza preferenzaList) {
		getPreferenzaList().add(preferenzaList);
		preferenzaList.setPoll(this);

		return preferenzaList;
	}

	public Preferenza removePreferenzaList(Preferenza preferenzaList) {
		getPreferenzaList().remove(preferenzaList);
		preferenzaList.setPoll(null);

		return preferenzaList;
	}

	public List<PreferenzaEspressa> getPreferenzaEspressaList() {
		return this.preferenzaEspressaList;
	}

	public void setPreferenzaEspressaList(List<PreferenzaEspressa> preferenzaEspressaList) {
		this.preferenzaEspressaList = preferenzaEspressaList;
	}

	public PreferenzaEspressa addPreferenzaEspressaList(PreferenzaEspressa preferenzaEspressaList) {
		getPreferenzaEspressaList().add(preferenzaEspressaList);
		preferenzaEspressaList.setPoll(this);

		return preferenzaEspressaList;
	}

	public PreferenzaEspressa removePreferenzaEspressaList(PreferenzaEspressa preferenzaEspressaList) {
		getPreferenzaEspressaList().remove(preferenzaEspressaList);
		preferenzaEspressaList.setPoll(null);

		return preferenzaEspressaList;
	}

    /**
     * @return the bodyPreferenzaList
     */
    public List<BodyPreferenza> getBodyPreferenzaList () {
        return bodyPreferenzaList;
    }

    /**
     * @param bodyPreferenzaList the bodyPreferenzaList to set
     */
    public void setBodyPreferenzaList ( List<BodyPreferenza> bodyPreferenzaList ) {
        this.bodyPreferenzaList = bodyPreferenzaList;
    }

    /**
     * @return the statoPoll
     */
    public StatoPoll getStatoPoll () {
        return statoPoll;
    }

    /**
     * @param statoPoll the statoPoll to set
     */
    public void setStatoPoll ( StatoPoll statoPoll ) {
        this.statoPoll = statoPoll;
    }

	public Integer getPreferenzeDaEsprimere() {
		return preferenzeDaEsprimere;
	}

	public void setPreferenzeDaEsprimere(Integer preferenzeDaEsprimere) {
		this.preferenzeDaEsprimere = preferenzeDaEsprimere;
	}

	/**
     * Creates builder to build {@link Poll}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link Poll}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private Long id;

        private String codice;

        private Date dtCreazione;

        private Date dtFineValidita;

        private Date dtInizioValidita;

        private String oggetto;

        private String testo;

        private List<Elettore> elettoreList = Collections.emptyList ();

        private Ente ente;

        private Presidente presidente;

        private List<Preferenza> preferenzaList = Collections.emptyList ();

        private List<PreferenzaEspressa> preferenzaEspressaList = Collections.emptyList ();

        private StatoPoll statoPoll;

        private List<BodyPreferenza> bodyPreferenzaList = Collections.emptyList ();
        
        private Integer preferenzeDaEsprimere;

        private Date dtUltimaNotifica;

        private Long frequenzaAggiornamento;

        private Builder () {
        }

        public Builder withId ( Long id ) {
            this.id = id;
            return this;
        }

        public Builder withCodice ( String codice ) {
            this.codice = codice;
            return this;
        }

        public Builder withDtCreazione ( Date dtCreazione ) {
            this.dtCreazione = dtCreazione;
            return this;
        }

        public Builder withDtFineValidita ( Date dtFineValidita ) {
            this.dtFineValidita = dtFineValidita;
            return this;
        }

        public Builder withDtInizioValidita ( Date dtInizioValidita ) {
            this.dtInizioValidita = dtInizioValidita;
            return this;
        }

        public Builder withOggetto ( String oggetto ) {
            this.oggetto = oggetto;
            return this;
        }

        public Builder withTesto ( String testo ) {
            this.testo = testo;
            return this;
        }

        public Builder withElettoreList ( List<Elettore> elettoreList ) {
            this.elettoreList = elettoreList;
            return this;
        }

        public Builder withEnte ( Ente ente ) {
            this.ente = ente;
            return this;
        }

        public Builder withPresidente ( Presidente presidente ) {
            this.presidente = presidente;
            return this;
        }

        public Builder withPreferenzaList ( List<Preferenza> preferenzaList ) {
            this.preferenzaList = preferenzaList;
            return this;
        }

        public Builder withPreferenzaEspressaList ( List<PreferenzaEspressa> preferenzaEspressaList ) {
            this.preferenzaEspressaList = preferenzaEspressaList;
            return this;
        }

        public Builder withStatoPoll ( StatoPoll statoPoll ) {
            this.statoPoll = statoPoll;
            return this;
        }

        public Builder withBodyPreferenzaList ( List<BodyPreferenza> bodyPreferenzaList ) {
            this.bodyPreferenzaList = bodyPreferenzaList;
            return this;
        }
        
        public Builder withPreferenzeDaEsprimere ( Integer preferenzeDaEsprimere ) {
            this.preferenzeDaEsprimere = preferenzeDaEsprimere;
            return this;
        }
        

        public Poll build () {
            return new Poll ( this );
        }
    }

}
