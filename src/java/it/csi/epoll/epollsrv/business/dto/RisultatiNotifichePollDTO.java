/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 *
 */

public class RisultatiNotifichePollDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1538635865442490834L;

    private PollDTO pollDTO;

    private List<PreferenzaEspressaDTO> preferenzeValide;

    private List<PreferenzaEspressaDTO> preferenzeNonValide;

    private List<PreferenzaEspressaDTO> schedeBianche;

    private List<ElettoreDTO> elettori;

    private List<ElettoreDTO> elettoriTotali;

    public RisultatiNotifichePollDTO ( PollDTO pollDto ) {

        if ( pollDto != null ) {
            this.pollDTO = pollDto;
            this.preferenzeValide = new LinkedList<> ();
            this.preferenzeNonValide = new LinkedList<> ();
            this.schedeBianche = new LinkedList<> ();
            this.elettori = new LinkedList<> ();
            this.elettoriTotali = new LinkedList<> ();

            if ( this.pollDTO.getPreferenzeEspresse () != null ) {
                for ( PreferenzaEspressaDTO preferenzaEspressa: this.pollDTO.getPreferenzeEspresse () ) {
                    if ( preferenzaEspressa.isValido () ) {
                        if ( !preferenzaEspressa.getPreferenza ().equals ( "" ) ) {
                            this.preferenzeValide.add ( preferenzaEspressa );
                        } else {
                            this.schedeBianche.add ( preferenzaEspressa );
                        }
                    } else {
                        this.preferenzeNonValide.add ( preferenzaEspressa );
                    }
                }
                Collections.sort ( this.preferenzeValide, new PreferenzaEspressaComparator () );
                Collections.sort ( this.preferenzeNonValide, new PreferenzaEspressaComparator () );

            }
            if ( !CollectionUtils.isEmpty ( pollDTO.getElettori () ) ) {
                pollDTO.getElettori ().stream ()
                    .forEach ( t -> {
                        if ( null != t.getDtDataVotazione () ) {
                            elettori.add ( t );
                        }
                        elettoriTotali.add ( t );
                    } );
                Collections.sort ( elettori, ( o1, o2 ) -> o1.getCognomeNome ().compareTo ( o2.getCognomeNome () ) );
                Collections.sort ( elettoriTotali, ( o1, o2 ) -> o1.getCognomeNome ().compareTo ( o2.getCognomeNome () ) );
            }
        }

    }

    private class PreferenzaEspressaComparator implements Comparator<PreferenzaEspressaDTO> {

        @Override
        public int compare ( PreferenzaEspressaDTO arg0, PreferenzaEspressaDTO arg1 ) {
            if ( ( null == arg0 ) && ( null == arg1 ) ) {
                return 0;
            }
            if ( null == arg0 ) {
                return -1;
            }
            if ( null == arg1 ) {
                return 1;
            }
            return arg1.getConteggio () - arg0.getConteggio ();
        }

    }

    public PollDTO getPollDTO () {
        return pollDTO;
    }

    public void setPollDTO ( PollDTO pollDTO ) {
        this.pollDTO = pollDTO;
    }

    public List<PreferenzaEspressaDTO> getPreferenzeValide () {
        return preferenzeValide;
    }

    public void setPreferenzeValide ( List<PreferenzaEspressaDTO> preferenzeValide ) {
        this.preferenzeValide = preferenzeValide;
    }

    public List<PreferenzaEspressaDTO> getPreferenzeNonValide () {
        return preferenzeNonValide;
    }

    public void setPreferenzeNonValide ( List<PreferenzaEspressaDTO> preferenzeNonValide ) {
        this.preferenzeNonValide = preferenzeNonValide;
    }

    public List<PreferenzaEspressaDTO> getSchedeBianche () {
        return schedeBianche;
    }

    public void setSchedeBianche ( List<PreferenzaEspressaDTO> schedeBianche ) {
        this.schedeBianche = schedeBianche;
    }

    public List<ElettoreDTO> getElettori () {
        return elettori;
    }

    public void setElettori ( List<ElettoreDTO> elettori ) {
        this.elettori = elettori;
    }

    public List<ElettoreDTO> getElettoriTotali () {
        return elettoriTotali;
    }

    public void setElettoriTotali ( List<ElettoreDTO> elettoriTotali ) {
        this.elettoriTotali = elettoriTotali;
    }

}
