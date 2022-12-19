/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util;

import java.util.Locale;


/**
 * Utility contenente tutte le costanti
 */
public interface Constants {

    /**
     * String boolean TRUE
     */
    String TRUE = "S";

    /**
     * String boolean FALSE
     */
    String FALSE = "N";

    Locale LOCAL_IT = new Locale ( "it", "IT" );

    /**
     * Path dove vengono salvati i template delle email
     */
    String TEMPLATE_PATH = "/templates";

    String UTF_8 = "UTF-8";

    /**
     * Email constants
     */

    String E_INFO_EMAIL = "Sono presenti: %d email ricevute";

    String E_TIPO_MAIL_PRESA_IN_CARICO = "accettazione";

    String E_TIPO_MAIL_RICEVUTA_CONSEGNA = "avvenuta-consegna";

    String E_TIPO_MAIL_MANCATA_CONSEGNA = "errore-consegna";

    String E_REGEX_EXTRACT_ADDRESS = "([\\w]([\\-\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})";

    String E_HEADER_X_RICEVUTA = "X-Ricevuta";

    String MIME_TYPE_MESSAGE_RFC822 = "message/rfc822";

    String MIME_TYPE_MULTIPART_GENERIC = "multipart/*";

    String CHARSET_UNKNOWN = "x-unknown";

    String CHARSET_ISO_8859 = "iso-8859-1";

    String NO_FILE_NAME_MESSAGE_RFC822 = "mail.eml";

    //LF header che dev'essere inserito nella mail di convocazione e che ci si aspetta di ricevere nella mail "preferenza espressa"
    String HEADER_REPLY_TO_POLL_INVITATION = "POLL_REPLY";

    String HEADER_IDENTIFICATIVO_SISTEMA = "IDENTIFICATIVO_SISTEMA";

    String OGGETTO_MAIL_INVITO_VOTAZIONE = "%s (%s)";

    String OGGETTO_RISULTATI = "Risultati scrutinio della votazione con codice: %s";

    String OGGETTO_NOTIFICA = "Notifica di aggiornamento della votazione con codice: %s";

    String BLANK = " ";

    String NUOVA_VOTAZIONE = "nuova votazione";

    String CHIUSURA_ANTICIPATA = "chiusura votazione";

    String PREFERENZA_ESPRESSA = "preferenza";
}
