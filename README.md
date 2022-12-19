# Prodotto
EPOLL
# Descrizione del prodotto
Il sistema gestirà il voto elettronico segreto a distanza per più enti.
      Ogni ente individuerà uno o più Presidenti di assemblea identificato
      univocamente da un indirizzo di email PEC. 


      Il Presidente potrà attivare una nuova votazione inviando una mail ad una
      casella PEC di sistema secondo un formato predefinito (vedi proposta in
      appendice A) indicando una serie di informazioni utili e necessarie alla
      gestione della votazione. Le informazioni richieste sono: un codice
      identificativo della votazione, l’elenco degli elettori individuati da
      nome, cognome ed indirizzo di email PEC, un testo che verrà usato per
      l’invito alla votazione, le preferenze indicabili dai votanti in fase di
      voto, la data e ora di inizio della votazione, la data e ora di chiusura
      della votazione. La mail di creazione di una nuova votazione prevederà che
      nell’oggetto compaia la dicitura “Nuova votazione” seguita dal codice
      attribuito alla votazione. 


      Il sistema, ricevuta la mail di richiesta creazione della votazione,
      effettuerà una validazione formale e, in caso di esito positivo, procederà
      a memorizzare i dati della nuova votazione, ad inviare la mail di ricevuta
      al Presidente e ad inviare le mail di invito ai singoli votanti. Nel caso
      in cui la validazione avesse esito negativo il sistema invierà al
      Presidente una mail con il dettaglio dell’errore riscontrato e non avvierà
      la votazione. 


      I partecipanti, una volta ricevuta la mail di invito alla votazione,
      risponderanno alla mail inviando la propria preferenza digitandola nel
      testo della mail di risposta. 


      Il sistema, ricevute le mail dei partecipanti, le analizzerà estraendo il
      testo inserito dai votanti e confrontandolo con le preferenze disponibili.
      Censirà quindi la preferenza espressa in una tabella apposita in cui
      vengono censite le preferenze espresse per tipologia e la loro numerosità.
      Queste informazioni non saranno relazionate con il votante. Le preferenze
      non previste dalla votazione verranno memorizzate come non valide. Le
      preferenze ricevute oltre il termine di chiusura della votazione verranno
      conteggiate come non valide. 
# Prerequisiti di sistema 
	Java version : 1.8
	Ant version : 1.7
# Versioning
	1.0.0
# Copyrights
	CSI Piemonte
# License 
	EUROPEAN UNION PUBLIC LICENCE v. 1.2