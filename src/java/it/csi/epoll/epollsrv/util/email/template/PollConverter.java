/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util.email.template;

import java.util.ArrayList;
import java.util.List;

import it.csi.epoll.epollsrv.business.dto.ElettoreDTO;
import it.csi.epoll.epollsrv.business.dto.EnteDTO;
import it.csi.epoll.epollsrv.business.dto.PollDTO;
import it.csi.epoll.epollsrv.business.dto.PreferenzaDTO;
import it.csi.epoll.epollsrv.business.dto.PreferenzaEspressaDTO;
import it.csi.epoll.epollsrv.business.dto.PresidenteDTO;
import it.csi.epoll.epollsrv.business.dto.StatoPollEnum;
import it.csi.epoll.epollsrv.integration.domain.Elettore;
import it.csi.epoll.epollsrv.integration.domain.Ente;
import it.csi.epoll.epollsrv.integration.domain.Poll;
import it.csi.epoll.epollsrv.integration.domain.Preferenza;
import it.csi.epoll.epollsrv.integration.domain.PreferenzaEspressa;


/**
 *
 */

public abstract class PollConverter {

    /**
     * Classe astratta new non permessa.
     */
    private PollConverter () {
        super ();
    }

    public static PollDTO pollEntityToDTO ( Poll inPoll ) {
        if ( inPoll == null ) {
            return null;
        }

        PollDTO outPoll = new PollDTO ();

        outPoll.setCodice ( inPoll.getCodice () );
        outPoll.setDtCreazione ( inPoll.getDtCreazione () );
        outPoll.setDtInizioValidita ( inPoll.getDtInizioValidita () );
        outPoll.setDtFineValidita ( inPoll.getDtFineValidita () );

        outPoll.setPresidente ( new PresidenteDTO () );
        outPoll.getPresidente ().setCognomeNome ( inPoll.getPresidente ().getCognomeNome () );
        outPoll.getPresidente ().setEmail ( inPoll.getPresidente ().getEmail () );
        outPoll.getPresidente ().setEnte ( enteEntityToDTO ( inPoll.getPresidente ().getEnte () ) );

        outPoll.setEnte ( enteEntityToDTO ( inPoll.getEnte () ) );

        if ( inPoll.getElettoreList () != null ) {
            List<ElettoreDTO> elettoriOut = new ArrayList<> ();
            for ( Elettore elettore: inPoll.getElettoreList () ) {
                elettoriOut.add ( elettoreEntityToDTO ( elettore ) );
            }
            outPoll.setElettori ( elettoriOut );
        }

        outPoll.setOggetto ( inPoll.getOggetto () );

        if ( inPoll.getPreferenzaList () != null ) {
            List<PreferenzaDTO> preferenzeOut = new ArrayList<> ();
            for ( Preferenza preferenza: inPoll.getPreferenzaList () ) {
                PreferenzaDTO preferenzaDTO = new PreferenzaDTO ();
                preferenzaDTO.setCodPreferenza ( preferenza.getCodPreferenza() );
                preferenzaDTO.setPreferenza ( preferenza.getPreferenza () );
                preferenzeOut.add ( preferenzaDTO );
            }
            outPoll.setPreferenze ( preferenzeOut );
        }

        if ( inPoll.getPreferenzaEspressaList () != null ) {
            List<PreferenzaEspressaDTO> preferenzeEspresseOut = new ArrayList<> ();
            for ( PreferenzaEspressa preferenza: inPoll.getPreferenzaEspressaList () ) {
                preferenzeEspresseOut.add ( preferenzaEspressaEntityToDTO ( preferenza ) );
            }
            outPoll.setPreferenzeEspresse ( preferenzeEspresseOut );
        }

        outPoll.setTesto ( inPoll.getTesto () );

        if ( ( inPoll.getStatoPoll () != null ) && ( null != inPoll.getStatoPoll ().getId () ) ) {
            for ( StatoPollEnum value: StatoPollEnum.values () ) {
                if ( inPoll.getStatoPoll ().getId ().equals ( value.getCodStato () ) ) {
                    outPoll.setStatoPoll ( value );
                    break;
                }
            }
        }
        
        outPoll.setMultiPreferenza(inPoll.getEnte().getAlgoritmoVotazione().getCodAlgoritmo().equals("MULTI") ? true : false);
        outPoll.setPreferenzeDaEsprimere(inPoll.getPreferenzeDaEsprimere());

        return outPoll;
    }

    private static PreferenzaEspressaDTO preferenzaEspressaEntityToDTO ( PreferenzaEspressa preferenza ) {
        if ( preferenza == null ) {
            return null;
        }

        PreferenzaEspressaDTO preferenzaOut = new PreferenzaEspressaDTO ();
        preferenzaOut.setConteggio ( preferenza.getConteggio () );
        preferenzaOut.setPreferenza ( preferenza.getPreferenza () );
        preferenzaOut.setValido ( preferenza.getValido () );

        return preferenzaOut;
    }

    private static ElettoreDTO elettoreEntityToDTO ( Elettore elettore ) {

        ElettoreDTO elettoreOut = new ElettoreDTO ();

        elettoreOut.setCognomeNome ( elettore.getCognomeNome () );
        elettoreOut.setDtDataVotazione ( elettore.getDtDataVotazione () );
        elettoreOut.setEmail ( elettore.getEmail () );

        return elettoreOut;
    }

    private static EnteDTO enteEntityToDTO ( Ente ente ) {

        EnteDTO enteOut = new EnteDTO ();

        enteOut.setId ( ente.getId () );
        enteOut.setCodice ( ente.getCodice () );
        enteOut.setCodFiscale ( ente.getCodFiscale () );

        return enteOut;
    }
}
