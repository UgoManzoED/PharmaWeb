*[Read this in English](README.md) | [Leggi in Italiano](README.it.md)*

# PharmaWeb - Piattaforma E-Commerce per Farmacia Online

**Università degli Studi di Salerno** **Corso:** Tecnologie Software per il Web - A.A. 2024/2025  
**Professore:** Simone ROMANO

**Team:** 
* Ugo Manzo (Matricola: 0512119071) - *Coordinatore / Backend & Database* - [GitHub](https://github.com/UgoManzoED)
* Davide Pio Lazzarini (Matricola: 0512119112) - *Frontend & UI/UX* - [GitHub](https://github.com/davidelazz)

> Piattaforma e-commerce completa progettata per facilitare la vendita online di farmaci da banco, integratori e prodotti per la cura personale, offrendo un'esperienza utente moderna e sicura.

![Project Status](https://img.shields.io/badge/Status-Active-success)
![Backend](https://img.shields.io/badge/Backend-Java%20EE-orange)
![Database](https://img.shields.io/badge/Database-MySQL-blue)
![Frontend](https://img.shields.io/badge/Frontend-Vanilla%20JS-yellow)

---

## Indice
* [Panoramica del Progetto](#panoramica-del-progetto)
* [Architettura e Sicurezza](#architettura-e-sicurezza)
* [Funzionalità Principali](#funzionalità-principali)
* [Tecnologie](#tecnologie)
* [Per Iniziare](#per-iniziare)
  * [Prerequisiti](#prerequisiti)
  * [Installazione](#installazione)
* [Credenziali di Test](#credenziali-di-test)
* [Documentazione](#documentazione)

---

## Panoramica del Progetto
**PharmaWeb** è un'applicazione web full-stack sviluppata per digitalizzare le operazioni di vendita al dettaglio di una farmacia. Il sistema offre un'esperienza di acquisto fluida per i clienti e una solida dashboard gestionale per gli amministratori, unendo il controllo dell'inventario al coinvolgimento dell'utente tramite funzionalità come la gestione asincrona del carrello e un sistema di punti fedeltà.

## Architettura e Sicurezza
Il progetto adotta rigorosamente il pattern architetturale **Model-View-Controller (MVC)**, garantendo una netta separazione tra logica di business, accesso ai dati (tramite pattern DAO) e livello di presentazione.

* **Integrità Dati e Performance:** Utilizzo di Viste SQL per astrarre la logica di calcolo complessa e Indici per l'ottimizzazione delle query.
* **Protezione CSRF:** Implementazione di un filtro centralizzato con rotazione automatica del token ad ogni richiesta POST.
* **Autenticazione:** Cifratura delle password tramite l'algoritmo **BCrypt** (via jBcrypt).
* **Prevenzione Injection:** Uso sistematico di `PreparedStatement` per mitigare le vulnerabilità di SQL Injection.
* **Sicurezza delle Viste:** Le pagine JSP sono protette e isolate all'interno della directory `/WEB-INF/` per impedirne l'accesso diretto via URL.

## Funzionalità Principali

### Area Cliente
**Catalogo Dinamico:** Esplorazione dei prodotti per categorie e ricerca testuale avanzata.\
**Carrello e Wishlist:** Operazioni asincrone tramite AJAX/Fetch API con persistenza su database per gli utenti autenticati.\
**Checkout Evoluto:** Elaborazione sicura delle transazioni con storicizzazione dei prezzi di acquisto e calcolo automatico dei punti fedeltà (1 punto ogni 20€ spesi).\
**Area Personale:** Dashboard dedicata per la gestione di indirizzi di spedizione, metodi di pagamento e consultazione dello storico ordini.

### Area Amministratore
**Gestione Catalogo (CRUD):** Inserimento, modifica e cancellazione dei prodotti con supporto per l'upload di immagini reali.\
**Monitoraggio Ordini:** Visualizzazione globale delle vendite della piattaforma con filtri avanzati per data ed email del cliente.\
**Gestione Utenti:** Elenco completo degli iscritti e funzionalità di assegnazione dei ruoli.

## Tecnologie
* **Backend:** [Java 17 LTS](https://www.oracle.com/java/), Jakarta EE 10 (Servlet, JSP, JSPF, Tag Files), [Apache Tomcat 10.1](https://tomcat.apache.org/), Maven.
* **Database:** [MySQL 8.0](https://www.mysql.com/), JDBC (`DriverManager`).
* **Frontend:** HTML5, CSS3 (Custom Grid System & Responsive Design), JavaScript (Vanilla), AJAX/Fetch API.
* **Librerie:** JSTL 2.0, FontAwesome 6, Google Gson.

---

## Per Iniziare
Segui queste istruzioni per configurare l'ambiente di sviluppo locale.

### Prerequisiti
* Java JDK 17
* Apache Tomcat 10.1
* MySQL Server 8.0
* Eclipse IDE for Enterprise Java and Web Developers (Consigliato)

### Installazione

1. **Setup del Database:**
   * Esegui lo script `BaseDati/schema.sql` per generare la struttura del database.
   * Esegui lo script `BaseDati/population.sql` per popolare il database con categorie e dati di test.

2. **Configurazione Backend:**
   * Apri il file `DriverManagerConnectionPool.java` e aggiorna le credenziali di connessione per farle corrispondere alla tua istanza MySQL locale.

3. **Deployment:**
   * Importa il progetto in Eclipse come "Existing Maven Project".
   * Associa il progetto all'ambiente di esecuzione del tuo server Apache Tomcat 10.1 locale.
   * Avvia il server in modalità *Debug* e accedi all'applicazione all'indirizzo: `http://localhost:8080/PharmaWeb/`

---

## Credenziali di Test
Utilizza le seguenti credenziali per esplorare i diversi livelli di accesso della piattaforma:

| Ruolo | Email | Password |
| :--- | :--- | :--- |
| **Amministratore** | `admin@pharmaweb.it` | `Password123!` |
| **Cliente** | `mario.rossi@email.com` | `Password123!` |

---

## Documentazione
Il **Website Design Document (WDD)** completo, che dettaglia le scelte architetturali e il design UI/UX, è disponibile nella cartella `Deliverables/`, insieme al **Javadoc** generato per l'intero codice sorgente del backend.
