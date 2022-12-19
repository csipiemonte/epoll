/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import java.math.BigInteger;

import javax.annotation.Generated;
import javax.mail.Folder;
import javax.mail.Store;

import org.hibernate.internal.util.Cloneable;


/**
 * @author abora
 * 
 *         Classe IMUTABILE contenente la configurazione della casella di posta per un ente.
 */
public final class EmailConfigDTO extends Cloneable {

    private BigInteger idEnte;

    private String password;

    private String username;

    private String serverHostnameIMAP;

    private String serverHostnameSMTP;

    private String indirizzoPec;

    private String inboxFolder;

    private boolean connectedIMAP;

    private boolean connectedSMTP;

    /**
     * Imap only
     */
    private Store store;

    /**
     * Imap only
     */
    private Folder inbox;

    @Generated ( "SparkTools" )
    private EmailConfigDTO ( Builder builder ) {
        this.idEnte = builder.idEnte;
        this.password = builder.password;
        this.username = builder.username;
        this.serverHostnameIMAP = builder.serverHostnameIMAP;
        this.serverHostnameSMTP = builder.serverHostnameSMTP;
        this.indirizzoPec = builder.indirizzoPec;
        this.inboxFolder = builder.inboxFolder;
        this.connectedIMAP = builder.connectedIMAP;
        this.connectedSMTP = builder.connectedSMTP;
        this.store = builder.store;
        this.inbox = builder.inbox;
    }

    /**
     * @return the serverHostnameIMAP
     */
    public String getServerHostnameIMAP () {
        return serverHostnameIMAP;
    }

    /**
     * @return the serverHostnameSMTP
     */
    public String getServerHostnameSMTP () {
        return serverHostnameSMTP;
    }

    /**
     * @return the idEnte
     */
    public BigInteger getIdEnte () {
        return idEnte;
    }

    /**
     * @return the inboxFolder
     */
    public String getInboxFolder () {
        return inboxFolder;
    }

    /**
     * @return the password
     */
    public String getPassword () {
        return password;
    }

    /**
     * @return the username
     */
    public String getUsername () {
        return username;
    }

    /**
     * @return the indirizzoPec
     */
    public String getIndirizzoPec () {
        return indirizzoPec;
    }

    /**
     * @return the store
     */
    public Store getStore () {
        return store;
    }

    /**
     * @return the inbox
     */
    public Folder getInbox () {
        return inbox;
    }

    /**
     * @return the connectedIMAP
     */
    public boolean isConnectedIMAP () {
        return connectedIMAP;
    }

    /**
     * @return the connectedSMTP
     */
    public boolean isConnectedSMTP () {
        return connectedSMTP;
    }

    /**
     * Creates builder to build {@link EmailConfigDTO}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link EmailConfigDTO}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private BigInteger idEnte;

        private String password;

        private String username;

        private String serverHostnameIMAP;

        private String serverHostnameSMTP;

        private String indirizzoPec;

        private String inboxFolder;

        private boolean connectedIMAP;

        private boolean connectedSMTP;

        private Store store;

        private Folder inbox;

        private Builder () {
        }

        public Builder withIdEnte ( BigInteger idEnte ) {
            this.idEnte = idEnte;
            return this;
        }

        public Builder withPassword ( String password ) {
            this.password = password;
            return this;
        }

        public Builder withUsername ( String username ) {
            this.username = username;
            return this;
        }

        public Builder withServerHostnameIMAP ( String serverHostnameIMAP ) {
            this.serverHostnameIMAP = serverHostnameIMAP;
            return this;
        }

        public Builder withServerHostnameSMTP ( String serverHostnameSMTP ) {
            this.serverHostnameSMTP = serverHostnameSMTP;
            return this;
        }

        public Builder withIndirizzoPec ( String indirizzoPec ) {
            this.indirizzoPec = indirizzoPec;
            return this;
        }

        public Builder withInboxFolder ( String inboxFolder ) {
            this.inboxFolder = inboxFolder;
            return this;
        }

        public Builder withConnectedIMAP ( boolean connectedIMAP ) {
            this.connectedIMAP = connectedIMAP;
            return this;
        }

        public Builder withConnectedSMTP ( boolean connectedSMTP ) {
            this.connectedSMTP = connectedSMTP;
            return this;
        }

        public Builder withStore ( Store store ) {
            this.store = store;
            return this;
        }

        public Builder withInbox ( Folder inbox ) {
            this.inbox = inbox;
            return this;
        }

        public EmailConfigDTO build () {
            return new EmailConfigDTO ( this );
        }
    }

}
