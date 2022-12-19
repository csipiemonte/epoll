/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import it.csi.epoll.epollsrv.business.dto.ConfigDTO;


/**
 * 
 */
@Configuration
@PropertySource ( "classpath:config.properties" )
public class EmailFuture {

    @Value ( "${email.connection.timeout}" )
    private String connectionTimeout;

    @Value ( "${email.timeout}" )
    private String emailTimeout;

    @Value ( "${email.debug}" )
    private String debug;

    @Value ( "${email.max.num.cfg.cached}" )
    private int maxNumEmailCfgCached;

    // Usare il vero valore in caso di unit test
    public static final String MAIL_PHRASE_ENCRYPT = "@@TO_REPLACE_BUIL_TIME@@";

    private ConfigDTO config;

    @Bean
    public ConfigDTO configDTO () {
        if ( null == config ) {
            config = ConfigDTO.builder ()
                .withConnectionTimeout ( connectionTimeout )
                .withDebug ( debug )
                .withEmailTimeout ( emailTimeout )
                .withMaxNumEmailCfgCached ( maxNumEmailCfgCached )
                .build ();
        }
        return config;
    }

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyConfigInDev () {
        return new PropertySourcesPlaceholderConfigurer ();
    }
}
