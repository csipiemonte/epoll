/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

import it.csi.epoll.epollsrv.business.dto.ConfigDTO;
import it.csi.epoll.epollsrv.business.dto.email.EmailConfigDTO;
import it.csi.epoll.epollsrv.util.ErrorMessages;


/**
 * @author abora
 * 
 * 
 *         Classe che permette di gestire le configurazioni multiente per le caselle di posta.
 */
public abstract class AbsEmailServices {

    /**
     * 
     */
    protected AbsEmailServices () {
        super ();
    }

    /**
     * Permette di avere una mappa con un numero di elementi massimo. Al raggiungimento di tale viene cancellato il meno recente.
     */
    protected static final Map<Long, EmailConfigDTO> cachedConfiguration = new LinkedHashMap<Long, EmailConfigDTO> () {

        /**
         *
         */
        private static final long serialVersionUID = -581183073192350209L;

        /**
         * Ridefinizione. Permette di cancellare, al raggiungimento del numero configurato, l'elemento meno recente.
         */
        @Override
        protected boolean removeEldestEntry ( Map.Entry<Long, EmailConfigDTO> entry ) {
            return size () > ConfigDTO.MAX_NUM_EMAIL_CFG_CACHED;
        }
    };

    /**
     * Metodo che restituisce la configurazione della casella di posta relativa all'ente
     * 
     * @param idEnte
     * @return PecCfg
     */
    protected EmailConfigDTO getPecConfigCached ( Long idEnte ) {
        EmailConfigDTO config = cachedConfiguration.get ( idEnte );
        if ( ( null == config ) || !config.isConnectedIMAP () ) {
            config = getConfig ( idEnte );
            Assert.notNull ( config, String.format ( ErrorMessages.E_ERRORE_CONFIG_NON_TROVATA, idEnte ) );
            // in caso di configurazione mancante vengono inseriti sulla mappa una per volta e solo se non presenti.
            synchronized ( cachedConfiguration ) {
                cachedConfiguration.put ( idEnte, config );
            }
        }
        return config;
    }

    /**
     * Metodo che permette di recuperare al configurazione dell'ente. Da implementare nella classe con contesto spring
     * 
     * @param idEnte identificativo univoco dell'ente.
     * @return EMailConfigDTO configurazione
     */
    protected abstract EmailConfigDTO getConfig ( Long idEnte );
}
