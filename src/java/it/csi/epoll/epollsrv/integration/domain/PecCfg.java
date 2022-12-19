/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.io.Serializable;

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

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Immutable;

import it.csi.epoll.epollsrv.config.EmailFuture;


/**
 * The persistent class for the EPOLL_T_PEC_CFG database table.
 * 
 */
@Immutable
@Entity
@Table(name="EPOLL_T_PEC_CFG")
@NamedQuery(name="PecCfg.findAll", query="SELECT p FROM PecCfg p")
public class PecCfg extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator ( name = "EPOLL_T_PEC_CFG_ID_GENERATOR", sequenceName = "EPOLL_T_PEC_CFG_ID_SEQ", allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EPOLL_T_PEC_CFG_ID_GENERATOR")
    private Long id;

	@Column(name="IMAP_INBOX_FOLDER")
	private String imapInboxFolder;

    @ColumnTransformer ( forColumn = "IMAP_PASSWORD", read = "pgp_sym_decrypt(IMAP_PASSWORD, '" + EmailFuture.MAIL_PHRASE_ENCRYPT + "')" )
	@Column(name="IMAP_PASSWORD")
	private byte[] imapPassword;

	@Column(name="IMAP_SERVER_HOSTNAME")
	private String imapServerHostname;

    @Column ( name = "IMAP_SERVER_PORT" )
    private int imapServerPort;

	@Column(name="IMAP_USERNAME")
	private String imapUsername;

	@Column(name="INDIRIZZO_PEC")
	private String indirizzoPec;

    @ColumnTransformer ( forColumn = "SMTP_PASSWORD", read = "pgp_sym_decrypt(SMTP_PASSWORD, '" + EmailFuture.MAIL_PHRASE_ENCRYPT + "')" )
	@Column(name="SMTP_PASSWORD")
	private byte[] smtpPassword;

	@Column(name="SMTP_SERVER_HOSTNAME")
	private String smtpServerHostname;

    @Column ( name = "SMTP_SERVER_PORT" )
    private int smtpServerPort;

	@Column(name="SMTP_USERNAME")
	private String smtpUsername;

	//bi-directional many-to-one association to Ente
	@ManyToOne
    @JoinColumn ( nullable = false, name = "ID_ENTE" )
	private Ente ente;

    @Generated ( "SparkTools" )
    private PecCfg ( Builder builder ) {
        this.id = builder.id;
        this.imapInboxFolder = builder.imapInboxFolder;
        this.imapPassword = builder.imapPassword;
        this.imapServerHostname = builder.imapServerHostname;
        this.imapServerPort = builder.imapServerPort;
        this.imapUsername = builder.imapUsername;
        this.indirizzoPec = builder.indirizzoPec;
        this.smtpPassword = builder.smtpPassword;
        this.smtpServerHostname = builder.smtpServerHostname;
        this.smtpServerPort = builder.smtpServerPort;
        this.smtpUsername = builder.smtpUsername;
        this.ente = builder.ente;
    }

    /**
     * 
     */
    public PecCfg () {
        super ();
    }

    @Override
    public Long getId () {
		return this.id;
	}

    public void setId ( Long id ) {
		this.id = id;
	}

	public String getImapInboxFolder() {
		return this.imapInboxFolder;
	}

	public void setImapInboxFolder(String imapInboxFolder) {
		this.imapInboxFolder = imapInboxFolder;
	}

	public byte[] getImapPassword() {
		return this.imapPassword;
	}

	public void setImapPassword(byte[] imapPassword) {
		this.imapPassword = imapPassword;
	}

	public String getImapServerHostname() {
		return this.imapServerHostname;
	}

	public void setImapServerHostname(String imapServerHostname) {
		this.imapServerHostname = imapServerHostname;
	}

	public String getImapUsername() {
		return this.imapUsername;
	}

	public void setImapUsername(String imapUsername) {
		this.imapUsername = imapUsername;
	}

	public String getIndirizzoPec() {
		return this.indirizzoPec;
	}

	public void setIndirizzoPec(String indirizzoPec) {
		this.indirizzoPec = indirizzoPec;
	}

	public byte[] getSmtpPassword() {
		return this.smtpPassword;
	}

	public void setSmtpPassword(byte[] smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getSmtpServerHostname() {
		return this.smtpServerHostname;
	}

	public void setSmtpServerHostname(String smtpServerHostname) {
		this.smtpServerHostname = smtpServerHostname;
	}

	public String getSmtpUsername() {
		return this.smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	public Ente getEnte() {
		return this.ente;
	}

	public void setEnte(Ente ente) {
		this.ente = ente;
	}

    /**
     * @return the imapServerPort
     */
    public int getImapServerPort () {
        return imapServerPort;
    }

    /**
     * @param imapServerPort the imapServerPort to set
     */
    public void setImapServerPort ( int imapServerPort ) {
        this.imapServerPort = imapServerPort;
    }

    /**
     * @return the smtpServerPort
     */
    public int getSmtpServerPort () {
        return smtpServerPort;
    }

    /**
     * @param smtpServerPort the smtpServerPort to set
     */
    public void setSmtpServerPort ( int smtpServerPort ) {
        this.smtpServerPort = smtpServerPort;
    }

    /**
     * Creates builder to build {@link PecCfg}.
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link PecCfg}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private Long id;

        private String imapInboxFolder;

        private byte [] imapPassword;

        private String imapServerHostname;

        private int imapServerPort;

        private String imapUsername;

        private String indirizzoPec;

        private byte [] smtpPassword;

        private String smtpServerHostname;

        private int smtpServerPort;

        private String smtpUsername;

        private Ente ente;

        private Builder () {
        }

        public Builder withId ( Long id ) {
            this.id = id;
            return this;
        }

        public Builder withImapInboxFolder ( String imapInboxFolder ) {
            this.imapInboxFolder = imapInboxFolder;
            return this;
        }

        public Builder withImapPassword ( byte [] imapPassword ) {
            this.imapPassword = imapPassword;
            return this;
        }

        public Builder withImapServerHostname ( String imapServerHostname ) {
            this.imapServerHostname = imapServerHostname;
            return this;
        }

        public Builder withImapServerPort ( int imapServerPort ) {
            this.imapServerPort = imapServerPort;
            return this;
        }

        public Builder withImapUsername ( String imapUsername ) {
            this.imapUsername = imapUsername;
            return this;
        }

        public Builder withIndirizzoPec ( String indirizzoPec ) {
            this.indirizzoPec = indirizzoPec;
            return this;
        }

        public Builder withSmtpPassword ( byte [] smtpPassword ) {
            this.smtpPassword = smtpPassword;
            return this;
        }

        public Builder withSmtpServerHostname ( String smtpServerHostname ) {
            this.smtpServerHostname = smtpServerHostname;
            return this;
        }

        public Builder withSmtpServerPort ( int smtpServerPort ) {
            this.smtpServerPort = smtpServerPort;
            return this;
        }

        public Builder withSmtpUsername ( String smtpUsername ) {
            this.smtpUsername = smtpUsername;
            return this;
        }

        public Builder withEnte ( Ente ente ) {
            this.ente = ente;
            return this;
        }

        public PecCfg build () {
            return new PecCfg ( this );
        }
    }

}
