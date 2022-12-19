/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.epoll.epollsrv.util;

/**
 * Interfaccia contente tutti i messaggi di errore nell'applicativo.
 */
@SuppressWarnings ( "all" )
public interface ErrorMessages {

    /**
     * GENERALI (G_|)
     */
    String G_PARAMETRO_OBBLIGATORIO_NON_SPECIFICATO = "Parametro %s obbligatorio non specificato";

    String G_PARAMETRO_ENTE_OBBLIGATORIO_NON_SPECIFICATO = "Parametro ENTE obbligatorio non specificato";

    String G_UTENTE_NON_DEFINITO = "UTENTE GENERICO";

    String G_ERRORE_LETTURA_CAMPO_CLASSE = "Error reading field %s from class %s";

    String G_ERRORE_GENERICO = "Errore generico";

    String G_OPERAZIONE_NON_CONSENTITA = "Operazione non consentita";

    /**
     * CRYPT (CRYPT_|)
     */
    String CRYPT_ERRORE_DURANTE_DECRIPTAZIONE = "Errore durante l'operazione di decrypt";

    String CRYPT_ERRORE_DURANTE_CRIPTAZIONE = "Errore durante l'operazione di encrypt";

    String CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_EN = "Errore durante l'inizializzazione del cipher di encryption";

    String CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_DEC = "Errore durante l'inizializzazione del cipher di decryption";

    String CRYPT_PASSPHRASE_NON_TROVATA_NELLE_PROPERTIES_ESTERNE = "passphrase non trovata nel file di properties esterno";

    String CRYPT_IVPARAMETER_NON_TROVATO_NELLE_PROPERTIES_ESTERNE = "ivparameter non trovato nel file di properties esterno";

    /**
     * EMAIL (E_|)
     */
    String E_ERRORE_CONNESSIONE = "Errore in fase di connessione alla casella email";

    String E_ERRORE_DISCONESSIONE = "Errore in fase di disconessione alla casella email";

    String E_ERRORE_CANCELLAZIONE = "Errore in fase di eliminazione del messaggio numero: %d con oggetto: %s";

    String E_NON_CONNESSO = "Non risulta una connessione attiva con la casella di posta";

    String E_INBOX_INESISTENTE = "Casella di posta: %s inesistente";

    String E_INBOX_NON_APERTA = "Casella di posta: %s non risulta in stato aperto";

    String E_INBOX_ERRORE_RICERCA = "Si e' verificato un errore in fase di ricerca della email";

    String E_ERRORE_PARSIFICAZIONE = "Errore in fase di parsificazione della email";

    String E_ERRORE_INVIO_MAIL = "Errore in fase di invio della mail.";

    String E_ERRORE_CHIUSURA_CONNESSIONE_SMTP = "Errore in fase di chiusura della connessione al serve di posta smtp.";

    String E_ERRORE_PARSIFICAZIONE_INDIRIZZO_EMAIL = "Errore in fase di parsificazione dell'indirizzo email: %s.";

    String E_ERRORE_PARSIFICAZIONE_INDIRIZZI_EMAIL = "Errore nessun indirizzo email da parsificare";

    String E_ERRORE_CONFIG_NON_TROVATA = "Errore nessuna configurazione Email trovata per l'ente: %s";

    String E_ERRORE_CONFIG_RECUPERO = "Errore in fase di recupero della configurazione Email trovata per l'ente: %s";

    String E_ERRORE_HEADER_NON_RIBALTATO = "Errore: impossibile ribaltare l'header: %s";

    String E_ERRORE_NOME_ALLEGATO = "Errore in fase di parsificazione del nome dell'allegato";

    String E_ERRORE_PARSIFICAZIONE_EMAIL = "Errore in fase di parsificazione del subject della email";

    String E_ERRORE_CONTEGGIO_EMAIL = "Errore in fase di conteggio del voto";

    String E_ERRORE_DATA_INIZIO_VALIDITA = "La data di inizio validita' non e' valida";

    String E_ERRORE_DATA_FINE_VALIDITA = "La data di fine validita' non e' valida";
    
    String E_ERRORE_CODICE_VOTAZIONE_TROPPO_LUNGO = "Il codice della votazione e' troppo lungo";
    
    String E_ERRORE_CODICE_PREFERENZA_TROPPO_LUNGO = "Un codice di preferenza e' troppo lungo";

    String E_ERRORE_CHIUSURA_ANTICIPATA = "Errore in fase di chiusura anticipata";

    String E_ERRORE_CHIUSURA_ANTICIPATA_POLL_MANCANTE = "Errore in fase di recupero del codice votazione: %s ";

    /**
     * PARSING EMAIL (P_|)
     */

    String P_ERRORE_NESSUN_MITTENTE_PRESENTE = "Nessun mittente presente";

    String P_ERRORE_MESSAGGIO_VUOTO = "Il messaggio e' vuoto";

    String P_ERRORE_NESSUN_OGGETTO_PRESENTE = "L'oggetto e' vuoto";

    String P_ERRORE_CODICE_POLL_NON_RICONOSCIUTO = "Il codice poll non e' stato riconosciuto correttamente";

    String P_ERRORE_MESSAGGIO_NON_ELABORATO = "Non e' possibile elaborare il messaggio";

    String P_ERRORE_EMAIL_PRIVA_DI_CONTENUTO = "Il messaggio e' privo di contenuto";

    String P_ERRORE_CONTENUTO_NON_RICONOSCIUTO = "Contenuto non riconosciuto";

    String P_ERRORE_FORMATO_NON_VALIDO = "Il formato del contenuto non e' valido";

    String P_ERRORE_PARAMETRO_NUM_MAX_NON_AMMESSO = "Il parametro numero massimo di preferenze non e' ammesso";
    
    String P_ERRORE_QUANTITA_PREFERENZE_INSUFFICIENTE = "numero preferenze insufficente rispetto al numero massimo esprimibile";
    
    String P_NUMERO_PREFERENZE_NON_VALIDO = "Numero massimo di preferenze esprimibili minore di 1";

    String P_ERRORE_POLL_NON_CREATO = "Errore in fase di creazione della votazione con oggetto %s";

    String P_ERRORE_CHIUSURA_NON_CONSENTITA = "La votazione che si sta tentando di chiudere risulta essere stata scrutinata";

    String P_DATA_RICEZIONE_MAIL_NON_TROVATA = "Data ricezione mail non trovata";
    
    String P_VOTO_NON_VALIDO = "Voto non valido";
    
    String P_SCHEDA_BIANCA = "Scheda bianca";

    String E_ERRORE_INVIO_NOTIFICHE = "Errore in fase di di parsing della cadenza dell'aggiornamento";

    /**
     * 
     */

    String DB_ELETTORE_NON_AMMESSO = "Elettore non ammesso per tale poll";

    String DB_PRESIDENTE_NON_PRESENTE_CON_SPECIFICA_EMAIL = "Nessun presidente individuato con l'email %s";

}
