# D&D Manager - JavaFX & Spring Boot Distributed App

## Descrizione del Progetto
**D&D Manager** è un'applicazione Java distribuita (Client-Server) pensata per i giocatori e i Dungeon Master di Dungeons & Dragons 5e. Permette di consultare i contenuti ufficiali del gioco e di creare, gestire e salvare contenuti "Homebrew" (personalizzati), come incantesimi, oggetti magici ed equipaggiamento. 

Il sistema è composto da un'interfaccia desktop (Frontend) che comunica tramite richieste HTTP RESTful con un servizio backend dedicato alla persistenza e all'elaborazione dei dati.

## Architettura e Funzionalità
Il progetto è strutturato rigorosamente secondo il paradigma Client-Server:

### Applicazione Client (Frontend)
* Sviluppata in **JavaFX**.
* **Gestione Asincrona:** Tutte le chiamate di rete verso il servizio sono gestite tramite *Thread* e *Executor* dedicati per non bloccare l'interfaccia grafica (UI) durante l'attesa delle risposte.
* **Funzionalità:**
  * Navigazione e filtraggio dei dati nel database (ricerca per nome, tipologia, ecc.).
  * Moduli per la creazione di nuovi contenuti Homebrew.
  * Gestione delle immagini per gli oggetti magici (conversione on-the-fly in stringhe Base64 per l'invio via JSON).
  * Inizializzazione del database e sincronizzazione dei dati.

### Servizio Backend
* Sviluppato in **Java Spring Boot**.
* **Database:** Espone un'API RESTful (formato JSON) interfacciata a un database **MySQL** per la persistenza dei dati.
* **Integrazione API Esterne:** Il servizio è in grado di interrogare la *D&D 5e SRD API* ufficiale per popolare dinamicamente il database con i contenuti canonici del gioco.
* **Sicurezza e DTO:** Utilizzo di oggetti DTO (come `MagicItemEncoded`) per gestire il payload JSON, convertendo lato server le immagini Base64 ricevute in `byte[]` per l'archiviazione nel database.

## Tecnologie utilizzate
* **Linguaggio Base:** Java
* **Frontend:** JavaFX
* **Backend:** Spring Boot, Spring Web
* **Database:** MySQL
* **Testing:** JUnit 5
* **Formato Dati:** JSON / Base64

## Installazione ed Esecuzione

Poiché l'architettura è distribuita, è necessario avviare prima il servizio backend e successivamente l'interfaccia client.

### 1. Avvio del Server (Spring Boot)
1. Assicurarsi di avere un'istanza MySQL in esecuzione (default locale: `127.0.0.1:3306`, user: `root`, pass: `root`).
2. Aprire la cartella `servizio`.
3. Il database verrà generato automaticamente all'avvio grazie al file `schema.sql` (in alternativa, eseguire lo script `createDatabase.sql` fornito).
4. Avviare l'applicazione Spring Boot.

### 2. Avvio del Client (JavaFX)
1. Aprire la cartella `applicazione`.
2. Avviare l'applicazione desktop.
3. Dal menu a tendina della home, selezionare **Initialize database** per scaricare i dati ufficiali dall'API di D&D e popolare il database locale.

## Testing
Il sistema Backend è coperto da Unit Test automatizzati basati su **JUnit**.
La suite di test verifica l'integrità delle operazioni CRUD:
* Connessione e inizializzazione del database.
* Aggiunta di incantesimi, oggetti magici ed equipaggiamento di test.
* Rimozione corretta dei dati.

## Documentazione
Per un'analisi approfondita degli endpoint REST, della struttura del database e dei pattern di conversione JSON, consulta la [Documentazione Tecnica Completa](docs/Documentazione-DnDManager.pdf).