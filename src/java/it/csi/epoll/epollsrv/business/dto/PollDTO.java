/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 *
 */

public class PollDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String codice;

    private Date dtCreazione;

    private Date dtFineValidita;

    private Date dtInizioValidita;

    private String oggetto;

    private String testo;

    private List<ElettoreDTO> elettori;

    private EnteDTO ente;

    private PresidenteDTO presidente;

    private List<PreferenzaDTO> preferenze;

    private List<PreferenzaEspressaDTO> preferenzeEspresse;
    
    private int preferenzeDaEsprimere;
    
    boolean multiPreferenza;
    
    private StatoPollEnum statoPoll;

    private Date dtUltimaNotifica;

    private Long frequenzaAggiornamento;

    public String getCodice () {
        return codice;
    }

    public void setCodice ( String codice ) {
        this.codice = codice;
    }

    public Date getDtCreazione () {
        return dtCreazione;
    }

    public void setDtCreazione ( Date dtCreazione ) {
        this.dtCreazione = dtCreazione;
    }

    public Date getDtFineValidita () {
        return dtFineValidita;
    }

    public void setDtFineValidita ( Date dtFineValidita ) {
        this.dtFineValidita = dtFineValidita;
    }

    public Date getDtInizioValidita () {
        return dtInizioValidita;
    }

    public void setDtInizioValidita ( Date dtInizioValidita ) {
        this.dtInizioValidita = dtInizioValidita;
    }

    public String getOggetto () {
        return oggetto;
    }

    public void setOggetto ( String oggetto ) {
        this.oggetto = oggetto;
    }

    public List<ElettoreDTO> getElettori () {
        return elettori;
    }

    public void setElettori ( List<ElettoreDTO> elettori ) {
        this.elettori = elettori;
    }

    public EnteDTO getEnte () {
        return ente;
    }

    public void setEnte ( EnteDTO ente ) {
        this.ente = ente;
    }

    public PresidenteDTO getPresidente () {
        return presidente;
    }

    public void setPresidente ( PresidenteDTO presidente ) {
        this.presidente = presidente;
    }

    public List<PreferenzaDTO> getPreferenze () {
        return preferenze;
    }

    public void setPreferenze ( List<PreferenzaDTO> preferenze ) {
        this.preferenze = preferenze;
    }

    public List<PreferenzaEspressaDTO> getPreferenzeEspresse () {
        return preferenzeEspresse;
    }

    public void setPreferenzeEspresse ( List<PreferenzaEspressaDTO> preferenzeEspresse ) {
        this.preferenzeEspresse = preferenzeEspresse;
    }


    @Override
    public String toString () {
        return "PollDTO [codice=" + codice
            + ", dtCreazione=" + dtCreazione
            + ", dtFineValidita=" + dtFineValidita
            + ", dtInizioValidita=" + dtInizioValidita
            + ", oggetto=" + oggetto
            + ", testo=" + testo
            + ", elettori=" + elettori
            + ", ente=" + ente
            + ", presidente=" + presidente
            + ", preferenze=" + preferenze
            + ", preferenzeEspresse=" + preferenzeEspresse
            + ", dtUltimaNotifica=" + dtUltimaNotifica
            + ", frequenzaAggiornamento=" + frequenzaAggiornamento
            + "]";
    }


    public String getTesto () {
        return testo;
    }


    public void setTesto ( String testo ) {
        this.testo = testo;
    }

    public StatoPollEnum getStatoPoll () {
        return statoPoll;
    }

    public void setStatoPoll ( StatoPollEnum statoPoll ) {
        this.statoPoll = statoPoll;
    }

	public int getPreferenzeDaEsprimere() {
		return preferenzeDaEsprimere;
	}

	public void setPreferenzeDaEsprimere(int preferenzeDaEsprimere) {
		this.preferenzeDaEsprimere = preferenzeDaEsprimere;
	}

	public boolean isMultiPreferenza() {
		return multiPreferenza;
	}

	public void setMultiPreferenza(boolean multiPreferenza) {
		this.multiPreferenza = multiPreferenza;
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

}
