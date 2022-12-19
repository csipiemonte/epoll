/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.epoll.epollsrv.integration.domain;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the epoll_t_body_preferenza database table.
 * 
 */
@Entity
@Table ( name = "epoll_t_body_preferenza" )
@NamedQuery ( name = "BodyPreferenza.findAll", query = "SELECT e FROM BodyPreferenza e" )
public class BodyPreferenza extends it.csi.epoll.epollsrv.integration.domain.AbstractEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column ( nullable = false, name = "uuid" )
    private String uuid;

    @Column ( name = "body_preferenza" )
    private String bodyPreferenza;

    //bi-directional many-to-one association to EpollTPoll
    @ManyToOne
    @JoinColumn ( name = "id_poll" )
    private Poll poll;

    @Generated ( "SparkTools" )
    private BodyPreferenza ( Builder builder ) {
        this.uuid = builder.uuid;
        this.bodyPreferenza = builder.bodyPreferenza;
        this.poll = builder.poll;
    }
    
    public BodyPreferenza() {
	}

    @Override
    public String getId () {
        return this.uuid;
    }


    public String getBodyPreferenza () {
        return this.bodyPreferenza;
    }

    public void setBodyPreferenza ( String bodyPreferenza ) {
        this.bodyPreferenza = bodyPreferenza;
    }

    /**
     * @return the poll
     */
    public Poll getPoll () {
        return poll;
    }

    /**
     * @param poll the poll to set
     */
    public void setPoll ( Poll poll ) {
        this.poll = poll;
    }
    
    public String getUuid () {
        return uuid;
    }

    public void setUuid ( String uuid ) {
        this.uuid = uuid;
    }

    @Override
	public String toString() {
        return "BodyPreferenza [id: " + uuid + ", preferenza: " + bodyPreferenza + ", poll: " + poll.getOggetto () + "]";
	}
    
    public String toStringPreferenza() {
		return bodyPreferenza;
	}

	/**
     * Creates builder to build {@link BodyPreferenza}.
     * 
     * @return created builder
     */
    @Generated ( "SparkTools" )
    public static Builder builder () {
        return new Builder ();
    }

    /**
     * Builder to build {@link BodyPreferenza}.
     */
    @Generated ( "SparkTools" )
    public static final class Builder {

        private String uuid;

        private String bodyPreferenza;

        private Poll poll;

        private Builder () {
        }

        public Builder withUuid ( String uuid ) {
            this.uuid = uuid;
            return this;
        }


        public Builder withBodyPreferenza ( String bodyPreferenza ) {
            this.bodyPreferenza = bodyPreferenza;
            return this;
        }

        public Builder withPoll ( Poll poll ) {
            this.poll = poll;
            return this;
        }

        public BodyPreferenza build () {
            return new BodyPreferenza ( this );
        }
    }


}
