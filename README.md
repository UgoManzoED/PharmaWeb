# PharmaWeb 💊

**PharmaWeb** è una piattaforma di e-commerce completa per una farmacia online, sviluppata come progetto per il corso di **Tecnologie Software per il Web** (A.A. 2024/2025) presso l'Università degli Studi di Salerno.

Il sistema permette agli utenti di acquistare farmaci da banco, integratori e prodotti per la cura personale, offrendo un'esperienza utente moderna e sicura.

## 👥 Membri del Gruppo
*   **Ugo Manzo** (Matricola: 0512119071) - *Coordinatore / Backend & Database*
*   **Davide Pio Lazzarini** (Matricola: 0512119112) - *Frontend & UI/UX*

---

## 🚀 Funzionalità Principali

### Area Cliente
- **Catalogo Dinamico:** Esplorazione prodotti per categorie e ricerca testuale.
- **Carrello & Wishlist:** Gestione asincrona (AJAX) dei prodotti con persistenza su database per utenti loggati.
- **Checkout Evoluto:** Transazione sicura con storicizzazione dei prezzi e calcolo punti fedeltà (1 punto ogni 20€ spesi).
- **Area Personale:** Dashboard per la gestione di indirizzi, metodi di pagamento e consultazione dello storico ordini.

### Area Amministratore
- **Gestione Catalogo (CRUD):** Inserimento, modifica e cancellazione di prodotti con supporto all'upload di immagini reali.
- **Monitoraggio Ordini:** Visualizzazione globale delle vendite con filtri avanzati per data ed email cliente.
- **Gestione Utenti:** Elenco completo degli iscritti e gestione dei ruoli.

---

## 🛠️ Stack Tecnologico

### Backend
- **Linguaggio:** Java 17 (LTS)
- **Tecnologia:** Jakarta EE 10 (Servlet, JSP, JSPF, Tag Files)
- **Server:** Apache Tomcat 10.1
- **Build Tool:** Maven

### Database
- **DBMS:** MySQL 8.0
- **Pattern:** DAO (Data Access Object) con gestione connessione tramite `DriverManager`
- **Integrità:** Viste SQL per logica di calcolo e Indici per l'ottimizzazione delle performance.

### Frontend
- **Linguaggi:** HTML5, CSS3 (Custom Grid System & Responsive Design), JavaScript (Vanilla)
- **Interattività:** AJAX / Fetch API per operazioni asincrone.
- **Librerie:** JSTL 2.0, FontAwesome 6, Google Gson.

---

## 🔒 Sicurezza e Buone Pratiche
- **Protezione CSRF:** Filtro centralizzato con rotazione automatica del token ad ogni richiesta POST.
- **Password Hashing:** Utilizzo dell'algoritmo **BCrypt** (via jBcrypt) per la cifratura delle password.
- **Prevenzione SQL Injection:** Uso sistematico di `PreparedStatement`.
- **Architettura MVC:** Rigorosa separazione tra logica di business, accesso ai dati e presentazione.
- **Sicurezza delle Viste:** Pagine JSP protette all'interno della directory `/WEB-INF/`.

---

## ⚙️ Installazione e Configurazione

1.  **Database:**
    - Eseguire lo script `BaseDati/schema.sql` per creare la struttura.
    - Eseguire `BaseDati/population.sql` per caricare i dati di test e le categorie.
2.  **Configurazione Java:**
    - Assicurarsi che la classe `DriverManagerConnectionPool.java` contenga le credenziali corrette per il proprio database locale.
3.  **Deployment:**
    - Importare il progetto in Eclipse come "Existing Maven Project".
    - Associare il progetto a un'istanza di **Apache Tomcat 10.1**.
    - Eseguire il server in modalità *Debug* e accedere a `http://localhost:8080/PharmaWeb/`.

---

## 🔑 Credenziali di Test

| Ruolo | Email | Password |
| :--- | :--- | :--- |
| **Amministratore** | `admin@pharmaweb.it` | `Password123!` |
| **Cliente** | `mario.rossi@email.com` | `Password123!` |

---

## 📄 Documentazione
Il **Website Design Document (WDD)** completo è disponibile nella cartella `Deliverables/`, insieme al **Javadoc** generato per l'intero backend.
