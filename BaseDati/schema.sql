-- ==========================================================================================
-- PharmaWeb Database Schema
-- Questo script crea il database da zero, definisce tutte le tabelle
-- e inserisce dati di esempio per il testing.
-- ==========================================================================================

-- --- 1. CREAZIONE E SELEZIONE DEL DATABASE ---
DROP DATABASE IF EXISTS pharmaweb_db;
CREATE DATABASE pharmaweb_db;
USE pharmaweb_db;

-- --- 2. CREAZIONE DELLE TABELLE ---

-- Tabella Categoria: Definisce le categorie dei prodotti
CREATE TABLE Categoria (
    ID_Categoria INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(100) NOT NULL UNIQUE
);

-- Tabella Utente: Memorizza i dati degli utenti registrati (clienti e admin)
CREATE TABLE Utente (
    ID_Utente INT PRIMARY KEY AUTO_INCREMENT,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Nome VARCHAR(50) NOT NULL,
    Cognome VARCHAR(50) NOT NULL,
    Ruolo ENUM('cliente', 'admin') NOT NULL,
    PuntiFedelta INT NOT NULL DEFAULT 0
);

-- Tabella IndirizzoSpedizione: Memorizza i vari indirizzi di spedizione dei clienti
CREATE TABLE IndirizzoSpedizione (
    ID_Indirizzo INT PRIMARY KEY AUTO_INCREMENT,
    NomeDestinatario VARCHAR(100) NOT NULL,
    Via VARCHAR(255) NOT NULL,
    Citta VARCHAR(100) NOT NULL,
    CAP VARCHAR(10) NOT NULL,
    Provincia VARCHAR(50),
    FK_Utente INT NOT NULL,
    FOREIGN KEY (FK_Utente) REFERENCES Utente(ID_Utente) ON DELETE CASCADE
);

-- Tabella MetodoPagamento: Memorizza i vari metodi di pagamento dei clienti
CREATE TABLE MetodoPagamento (
    ID_Metodo INT PRIMARY KEY AUTO_INCREMENT,
    TipoCarta ENUM('Visa', 'Mastercard', 'Paypal') NOT NULL,
    Titolare VARCHAR(100) NOT NULL,
    Ultime4Cifre CHAR(4) NOT NULL,
    MeseScadenza INT NOT NULL,
    AnnoScadenza INT NOT NULL,
    FK_Utente INT NOT NULL,
    FOREIGN KEY (FK_Utente) REFERENCES Utente(ID_Utente) ON DELETE CASCADE
);

-- Tabella Prodotto: Contiene le informazioni sui prodotti in vendita
CREATE TABLE Prodotto (
    ID_Prodotto INT PRIMARY KEY AUTO_INCREMENT,
    Nome VARCHAR(255) NOT NULL,
    Descrizione TEXT,
    Prezzo DECIMAL(10, 2) NOT NULL,
    ScontoPercentuale INT NOT NULL DEFAULT 0,
    QuantitaDisponibile INT NOT NULL DEFAULT 0,
    URL_Immagine VARCHAR(255),
    Attivo BOOLEAN NOT NULL DEFAULT TRUE,
    FK_Categoria INT,
    FOREIGN KEY (FK_Categoria) REFERENCES Categoria(ID_Categoria) ON DELETE SET NULL
);

-- Tabella Ordine: Contiene le informazioni generali di un acquisto
CREATE TABLE Ordine (
    ID_Ordine INT PRIMARY KEY AUTO_INCREMENT,
    DataOrdine DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ImportoTotale DECIMAL(10, 2) NOT NULL,
    IndirizzoSpedizione VARCHAR(255) NOT NULL, -- Storicizza l'indirizzo dell'ordine
    MetodoPagamentoUtilizzato VARCHAR(255) NOT NULL, -- Storicizza il metodo di pagamento dell'ordine
    Stato ENUM('In elaborazione', 'Spedito', 'Consegnato', 'Annullato') NOT NULL,
    PuntiGuadagnati INT NOT NULL DEFAULT 0,
    PuntiUtilizzati INT NOT NULL DEFAULT 0,
    FK_Utente INT,
    FOREIGN KEY (FK_Utente) REFERENCES Utente(ID_Utente) ON DELETE SET NULL -- Mantiene lo storico ordini anche se l'utente viene cancellato
);

-- Tabella Dettaglio_Ordine: Entità associativa per la relazione N:M tra Ordine e Prodotto
CREATE TABLE RigaOrdine (
    FK_Ordine INT,
    FK_Prodotto INT,
    Quantita INT NOT NULL,
    PrezzoAcquisto DECIMAL(10, 2) NOT NULL, -- Storicizza il prezzo al momento dell'acquisto
    NomeProdottoSnapshot VARCHAR(255) NOT NULL, -- Congela il nome
    IvaApplicata INT NOT NULL DEFAULT 22,       -- Congela l'IVA
    PRIMARY KEY (FK_Ordine, FK_Prodotto),
    FOREIGN KEY (FK_Ordine) REFERENCES Ordine(ID_Ordine) ON DELETE CASCADE, -- Se si cancella un ordine, si cancellano i dettagli
    FOREIGN KEY (FK_Prodotto) REFERENCES Prodotto(ID_Prodotto)
);

-- Tabella Recensione: Memorizza le recensioni dei prodotti
CREATE TABLE Recensione (
    ID_Recensione INT PRIMARY KEY AUTO_INCREMENT,
    Voto INT NOT NULL CHECK (Voto >= 1 AND Voto <= 5),
    Testo TEXT,
    DataRecensione DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FK_Utente INT,
    FK_Prodotto INT,
    FOREIGN KEY (FK_Utente) REFERENCES Utente(ID_Utente) ON DELETE CASCADE, -- Se l'utente è cancellato, le sue recensioni spariscono
    FOREIGN KEY (FK_Prodotto) REFERENCES Prodotto(ID_Prodotto) ON DELETE CASCADE -- Se il prodotto è cancellato, le sue recensioni spariscono
);

-- Tabella Movimento_Punti: Log di tutte le transazioni di punti fedeltà
CREATE TABLE StoricoPunti (
    ID_Movimento INT PRIMARY KEY AUTO_INCREMENT,
    Punti INT NOT NULL, -- Positivo per accredito, negativo per addebito
    DataMovimento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Tipo VARCHAR(50) NOT NULL, -- es. 'ACCREDITO_ORDINE', 'UTILIZZO_SCONTO'
    FK_Utente INT,
    FK_Ordine INT NULL, -- Può essere nullo (es. bonus di iscrizione)
    FOREIGN KEY (FK_Utente) REFERENCES Utente(ID_Utente) ON DELETE CASCADE,
    FOREIGN KEY (FK_Ordine) REFERENCES Ordine(ID_Ordine) ON DELETE SET NULL
);

-- Tabella CarrelloPersistente: Memorizza il carrello degli utenti loggati per la persistenza tra sessioni
CREATE TABLE CarrelloPersistente (
    FK_Utente INT NOT NULL,
    FK_Prodotto INT NOT NULL,
    Quantita INT NOT NULL DEFAULT 1,
    PRIMARY KEY (FK_Utente, FK_Prodotto),
    FOREIGN KEY (FK_Utente) REFERENCES Utente(ID_Utente) ON DELETE CASCADE,
    FOREIGN KEY (FK_Prodotto) REFERENCES Prodotto(ID_Prodotto) ON DELETE CASCADE
);

-- Tabella WishlistPersistente: Memorizza la wishlist degli utenti loggati per la persistenza tra sessioni
CREATE TABLE WishlistPersistente (
    FK_Utente INT NOT NULL,
    FK_Prodotto INT NOT NULL,
    PRIMARY KEY (FK_Utente, FK_Prodotto),
    FOREIGN KEY (FK_Utente) REFERENCES Utente(ID_Utente) ON DELETE CASCADE,
    FOREIGN KEY (FK_Prodotto) REFERENCES Prodotto(ID_Prodotto) ON DELETE CASCADE
);

-- --- 3. CREAZIONE DEGLI INDICI PER OTTIMIZZARE LE PERFORMANCE ---

-- Indice sulla chiave esterna dell'utente nella tabella Ordine
CREATE INDEX idx_ordine_utente ON Ordine(FK_Utente);

-- Indici sulle chiavi esterne della tabella RigaOrdine
CREATE INDEX idx_rigaordine_ordine ON RigaOrdine(FK_Ordine);
CREATE INDEX idx_rigaordine_prodotto ON RigaOrdine(FK_Prodotto);

-- Indici sulle chiavi esterne della tabella Recensione
CREATE INDEX idx_recensione_utente ON Recensione(FK_Utente);
CREATE INDEX idx_recensione_prodotto ON Recensione(FK_Prodotto);

-- Indici sulla chiave esterna dell'utente nella tabella IndirizzoSpedizione
CREATE INDEX idx_indirizzo_utente ON IndirizzoSpedizione(FK_Utente);

-- Indici sulla chiave esterna dell'utente nella tabella MetodoPagamento
CREATE INDEX idx_pagamento_utente ON MetodoPagamento(FK_Utente);

-- Indici per il carrello e la wishlist persistente
CREATE INDEX idx_carrello_utente ON CarrelloPersistente(FK_Utente);
CREATE INDEX idx_wishlist_utente ON WishlistPersistente(FK_Utente);

-- --- 4. CREAZIONE DI VISTE PER SEMPLIFICARE LE QUERY ---

-- Vista dettagli di un ordine
CREATE VIEW VistaDettaglioOrdineCompleto AS
SELECT
    o.ID_Ordine,
    o.DataOrdine,
    u.Email AS EmailCliente,
    ro.NomeProdottoSnapshot AS NomeProdotto,
    ro.Quantita,
    ro.PrezzoAcquisto,
    (ro.Quantita * ro.PrezzoAcquisto) AS SubtotaleRiga,
    ro.IvaApplicata
FROM Ordine o
JOIN Utente u ON o.FK_Utente = u.ID_Utente
JOIN RigaOrdine ro ON o.ID_Ordine = ro.FK_Ordine;

-- Vista Catalogo con nome e prezzo finale del prodotto
CREATE VIEW VistaCatalogoProdotti AS
SELECT
    p.ID_Prodotto,
    p.Nome AS NomeProdotto,
    p.Descrizione,
    p.Prezzo AS PrezzoDiListino,
    p.ScontoPercentuale,
    -- Calcoliamo il prezzo finale scontato
    CASE
        WHEN p.ScontoPercentuale > 0
        THEN p.Prezzo * (1 - p.ScontoPercentuale / 100.0)
        ELSE p.Prezzo
    END AS PrezzoFinale,
    p.QuantitaDisponibile,
    p.URL_Immagine,
    p.Attivo,
    c.ID_Categoria,
    c.Nome AS NomeCategoria
FROM
    Prodotto p
LEFT JOIN Categoria c ON p.FK_Categoria = c.ID_Categoria;

-- Lista di tutti gli ordini
CREATE VIEW VistaRiepilogoOrdini AS
SELECT
    o.ID_Ordine,
    o.DataOrdine,
    o.ImportoTotale,
    o.Stato,
    u.ID_Utente,
    u.Email AS EmailCliente,
    CONCAT(u.Nome, ' ', u.Cognome) AS NomeCliente,
    o.IndirizzoSpedizione,
    o.MetodoPagamentoUtilizzato
FROM
    Ordine o
JOIN Utente u ON o.FK_Utente = u.ID_Utente;

-- Recupera le recensioni con nome prodotto e nome utente
CREATE VIEW VistaRecensioniComplete AS
SELECT
    r.ID_Recensione,
    r.Voto,
    r.Testo,
    r.DataRecensione,
    p.ID_Prodotto,
    p.Nome AS NomeProdotto,
    u.ID_Utente,
    CONCAT(u.Nome, ' ', SUBSTRING(u.Cognome, 1, 1), '.') AS NomeUtenteVisibile -- Es. "Mario R." per privacy
FROM
    Recensione r
JOIN Prodotto p ON r.FK_Prodotto = p.ID_Prodotto
JOIN Utente u ON r.FK_Utente = u.ID_Utente;