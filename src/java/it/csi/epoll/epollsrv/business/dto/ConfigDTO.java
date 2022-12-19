/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto;

import java.io.Serializable;

import javax.annotation.Generated;


/**
 *
 */
@SuppressWarnings ( "all" )
public class ConfigDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -511431797539640059L;

    public static final String PROTOCOL_IMAP = "imap";

    public static final String PROTOCOL_SMTP = "smtp";

    public static final String MAIL_PREFIX = "mail.";

    public static int MAX_NUM_EMAIL_CFG_CACHED = 10;

    private String connectionTimeout;

    private String emailTimeout;

    private String debug;

    private int maxNumEmailCfgCached;

    /**
     * 
     */
    private ConfigDTO () {
        super ();
    }

    @Generated ( "SparkTools" )
    private ConfigDTO ( Builder builder ) {
        this.connectionTimeout = builder.connectionTimeout;
        this.emailTimeout = builder.emailTimeout;
        this.debug = builder.debug;
        this.maxNumEmailCfgCached = builder.maxNumEmailCfgCached;
        this.MAX_NUM_EMAIL_CFG_CACHED = builder.maxNumEmailCfgCached;
    }

    /**
     * @return the connectionTimeout
     */
    public String getConnectionTimeout () {
        return connectionTimeout;
    }

    /**
     * @return the emailTimeout
     */
    public String getEmailTimeout () {
        return emailTimeout;
    }

    /**
     * @return the debug
     */
    public String getDebug () {
        return debug;
    }

    /**
     * @return the maxNumEmailCfgCached
     */
    public int getMaxNumEmailCfgCached () {
        return maxNumEmailCfgCached;
    }

    /**
     * Creates builder to build {@link ConfigDTO}.
     * 
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link ConfigDTO}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private String connectionTimeout;

        private String emailTimeout;

        private String debug;

        private int maxNumEmailCfgCached;

        private Builder () {
        }

        public Builder withConnectionTimeout ( String connectionTimeout ) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder withEmailTimeout ( String emailTimeout ) {
            this.emailTimeout = emailTimeout;
            return this;
        }

        public Builder withDebug ( String debug ) {
            this.debug = debug;
            return this;
        }

        public Builder withMaxNumEmailCfgCached ( int maxNumEmailCfgCached ) {
            this.maxNumEmailCfgCached = maxNumEmailCfgCached;
            return this;
        }

        public ConfigDTO build () {
            return new ConfigDTO ( this );
        }
    }

}
