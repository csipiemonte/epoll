/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.business.dto.email;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.Message;


/**
 * @author abora.
 * 
 * 
 *         Classe usate per inviare e ricercare email
 */
public class EmailDTO extends EmailEnte {

    /**
     *
     */
    private static final long serialVersionUID = -6366309092321981657L;

    private String body;

    private String oggetto;

    private List<String> to;

    private List<String> cc;

    private String from;

    private String tipoMail;

    private transient Message emailColl;

    private List<AllegatoDTO> allegati;

    private transient Map<String, String> headers;

    private MimeType mimeType;

    private Date sentDate;

    private Date receivedDate;

    private boolean nuovoPoll;

    /**
     * Costruttore per ente
     * 
     * @param idEnte
     */
    public EmailDTO ( Long idEnte ) {
        super ( idEnte );
    }

    public String getBody () {
        return body;
    }

    public void setBody ( String body ) {
        this.body = body;
    }

    public String getOggetto () {
        return oggetto;
    }

    public void setOggetto ( String oggetto ) {
        this.oggetto = oggetto;
    }

    public List<String> getTo () {
        return to;
    }

    public void setTo ( List<String> to ) {
        this.to = to;
    }

    public List<String> getCc () {
        return cc;
    }

    public void setCc ( List<String> cc ) {
        this.cc = cc;
    }

    public String getFrom () {
        return from;
    }

    public void setFrom ( String from ) {
        this.from = from;
    }

    public String getTipoMail () {
        return tipoMail;
    }

    public void setTipoMail ( String tipoMail ) {
        this.tipoMail = tipoMail;
    }

    public Message getEmailColl () {
        return emailColl;
    }

    public void setEmailColl ( Message emailColl ) {
        this.emailColl = emailColl;
    }

    /**
     * @return the allegati
     */
    public List<AllegatoDTO> getAllegati () {
        return allegati;
    }

    /**
     * @param allegati the allegati to set
     */
    public void setAllegati ( List<AllegatoDTO> allegati ) {
        this.allegati = allegati;
    }

    public String getHeader ( String key ) {
        String value = null;
        if ( this.headers != null ) {
            return this.headers.get ( key );
        }

        return value;
    }

    public MimeType getMimeType () {
        return mimeType;
    }

    public void setMimeType ( MimeType mimeType ) {
        this.mimeType = mimeType;
    }

    public Map<String, String> getHeaders () {
        return headers;
    }

    public void setHeaders ( Map<String, String> headers ) {
        this.headers = headers;
    }

    public Date getSentDate () {
        return sentDate;
    }

    public void setSentDate ( Date sentDate ) {
        this.sentDate = sentDate;
    }

    /**
     * @return the receivedDate
     */
    public Date getReceivedDate () {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate ( Date receivedDate ) {
        this.receivedDate = receivedDate;
    }

    /**
     * @return the nuovoPoll
     */
    public boolean isNuovoPoll () {
        return nuovoPoll;
    }

    /**
     * @param nuovoPoll the nuovoPoll to set
     */
    public void setNuovoPoll ( boolean nuovoPoll ) {
        this.nuovoPoll = nuovoPoll;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString () {
        final int maxLen = 10;
        return String.format (
            "EmailDTO [body=%s, oggetto=%s, to=%s, cc=%s, from=%s, tipoMail=%s, allegati=%s, headers=%s, mimeType=%s, sentDate=%s, receivedDate=%s, nuovoPoll=%s, getIdEnte()=%s]",
            body, oggetto, to != null ? toString ( to, maxLen ) : null, cc != null ? toString ( cc, maxLen ) : null, from, tipoMail,
            allegati != null ? toString ( allegati, maxLen ) : null, headers != null ? toString ( headers.entrySet (), maxLen ) : null, mimeType, sentDate,
            receivedDate, nuovoPoll, super.toString () );
    }

    private String toString ( Collection<?> collection, int maxLen ) {
        StringBuilder builder = new StringBuilder ();
        builder.append ( "[" );
        int i = 0;
        for ( Iterator<?> iterator = collection.iterator (); iterator.hasNext () && ( i < maxLen ); i++ ) {
            if ( i > 0 ) {
                builder.append ( ", " );
            }
            builder.append ( iterator.next () );
        }
        builder.append ( "]" );
        return builder.toString ();
    }
}
