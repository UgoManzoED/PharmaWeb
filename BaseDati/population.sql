-- ==========================================================================================
-- PharmaWeb Database Population Script (Versione Estesa)
-- Password per tutti gli utenti: Password123!
-- ==========================================================================================

USE pharmaweb_db;

-- --- 1. POPOLAMENTO CATEGORIE ---
INSERT INTO Categoria (Nome) VALUES
('Integratori e Vitamine'),          -- ID 1
('Cosmetica e Bellezza'),            -- ID 2
('Farmaci da Banco'),                -- ID 3
('Infanzia e Bambini'),              -- ID 4
('Igiene e Cura Personale'),         -- ID 5
('Dispositivi Medici');              -- ID 6

-- --- 2. POPOLAMENTO UTENTI ---
-- Password hashata con BCrypt per "Password123!"
SET @pass = '$2a$12$fY2A5QoA8DGn8ep0EOP/UuxGZOqBw0e3tQxQjqGQHNXmONIOnJ6r6';

INSERT INTO Utente (Email, Password, Nome, Cognome, Ruolo, PuntiFedelta) VALUES
('admin@pharmaweb.it', @pass, 'Admin', 'Pharma', 'admin', 0),                                 -- ID 1
('mario.rossi@email.com', @pass, 'Mario', 'Rossi', 'cliente', 150),                        -- ID 2
('laura.bianchi@email.com', @pass, 'Laura', 'Bianchi', 'cliente', 250),                      -- ID 3
('giuseppe.verdi@email.com', @pass, 'Giuseppe', 'Verdi', 'cliente', 25),                   -- ID 4
('anna.neri@email.com', @pass, 'Anna', 'Neri', 'cliente', 80),                             -- ID 5
('luca.bruni@email.com', @pass, 'Luca', 'Bruni', 'cliente', 10);                           -- ID 6

-- --- 3. INDIRIZZI E PAGAMENTI ---
INSERT INTO IndirizzoSpedizione (NomeDestinatario, Via, Citta, CAP, Provincia, FK_Utente) VALUES
('Mario Rossi - Casa', 'Via Roma 1', 'Napoli', '80121', 'NA', 2),
('Mario Rossi - Ufficio', 'Corso Umberto I 25', 'Napoli', '80138', 'NA', 2),
('Laura Bianchi', 'Piazza del Popolo 10', 'Roma', '00187', 'RM', 3),
('Giuseppe Verdi', 'Via Dante Alighieri 5', 'Milano', '20121', 'MI', 4);

INSERT INTO MetodoPagamento (TipoCarta, Titolare, Ultime4Cifre, MeseScadenza, AnnoScadenza, FK_Utente) VALUES
('Visa', 'Mario Rossi', '1234', 12, 2026, 2),
('Mastercard', 'Laura Bianchi', '5678', 6, 2027, 3),
('Paypal', 'Giuseppe Verdi', '9012', 3, 2028, 4);

-- --- 4. CATALOGO PRODOTTI (ESTESO) ---
INSERT INTO Prodotto (Nome, Descrizione, Prezzo, ScontoPercentuale, QuantitaDisponibile, URL_Immagine, FK_Categoria) VALUES
-- Integratori (Cat 1)
('Vitamina C 1000mg', 'Integratore di Vitamina C purissima per le difese immunitarie.', 12.00, 10, 80, 'img/vitamina-c.jpg', 1),
('Magnesio Supremo', 'Integratore di magnesio in polvere solubile, formula originale.', 15.50, 0, 60, 'img/magnesio-supremo.jpg', 1),
('Omega 3 Extra', 'Acidi grassi purificati per il benessere cardiovascolare.', 25.00, 15, 45, 'img/omega-3.jpg', 1),
('Multivitaminico A-Z', 'Complesso completo di vitamine e minerali per lo sport.', 18.90, 5, 100, 'img/multivitam.jpg', 1),
-- Cosmetica (Cat 2)
('Crema Viso Idratante', 'Trattamento idratante intensivo per pelli secche e sensibili.', 22.99, 0, 50, 'img/crema-viso.jpg', 2),
('Siero Anti-età', 'Siero rigenerante all acido ialuronico e collagene.', 35.50, 20, 30, 'img/siero-antieta.jpg', 2),
('Shampoo Delicato', 'Shampoo per uso quotidiano, rispetta il PH del cuoio capelluto.', 8.90, 0, 120, 'img/shampoo.jpg', 2),
('Crema Mani Karitè', 'Protezione intensiva per mani screpolate dal freddo.', 5.50, 0, 200, 'img/crema-mani.jpg', 2),
-- Farmaci da Banco (Cat 3)
('Paracetamolo 500mg', 'Analgesico ed antipiretico per febbre e dolori lievi.', 7.50, 0, 100, 'img/paracetamolo.jpg', 3),
('Ibuprofene 400mg', 'Antinfiammatorio per dolori articolari e muscolari.', 9.20, 10, 90, 'img/ibuprofene.jpg', 3),
('Sciroppo Mucolitico', 'Sciroppo per tosse grassa, gusto menta.', 11.00, 0, 55, 'img/sciroppo.jpg', 3),
-- Infanzia (Cat 4)
('Biberon Neonato', 'Biberon ergonomico anti-colica 250ml.', 9.90, 0, 40, 'img/biberon.jpg', 4),
('Pasta Protettiva', 'Pasta barriera per il cambio pannolino con ossido di zinco.', 6.50, 0, 70, 'img/pasta-protettiva.jpg', 4),
('Latte in Polvere 1', 'Alimento completo per neonati da 0 a 6 mesi.', 21.00, 5, 30, 'img/latte-polvere.jpg', 4),
-- Igiene (Cat 5)
('Collutorio Antibatterico', 'Protezione completa per denti e gengive sane.', 5.80, 5, 150, 'img/collutorio.jpg', 5),
('Spazzolino Elettrico', 'Tecnologia oscillante-rotante per una pulizia profonda.', 45.00, 0, 25, 'img/spazzolino.jpg', 5),
-- Dispositivi Medici (Cat 6)
('Termometro Digitale', 'Misurazione rapida in 10 secondi con segnale acustico.', 11.50, 0, 60, 'img/termometro.jpg', 6),
('Misuratore Pressione', 'Sfigmomanometro da braccio automatico con display XL.', 39.90, 10, 35, 'img/misuratore-pressione.jpg', 6),
('Saturimetro da Dito', 'Monitoraggio ossigeno e frequenza cardiaca.', 24.00, 25, 20, 'img/saturimetro.jpg', 6);

-- --- 5. ORDINI E DETTAGLI ---
-- Ordine 1
INSERT INTO Ordine (ImportoTotale, IndirizzoSpedizione, MetodoPagamentoUtilizzato, Stato, PuntiGuadagnati, FK_Utente) VALUES
(27.30, 'Mario Rossi - Casa, Via Roma 1, Napoli', 'Visa terminante in 1234', 'Consegnato', 1, 2);
INSERT INTO RigaOrdine (FK_Ordine, FK_Prodotto, Quantita, PrezzoAcquisto) VALUES (1, 9, 2, 7.50), (1, 3, 1, 12.30);

-- Ordine 2
INSERT INTO Ordine (ImportoTotale, IndirizzoSpedizione, MetodoPagamentoUtilizzato, Stato, PuntiGuadagnati, PuntiUtilizzati, FK_Utente) VALUES
(27.30, 'Laura Bianchi, Piazza del Popolo 10, Roma', 'Mastercard terminante in 5678', 'Spedito', 1, 10, 3);
INSERT INTO RigaOrdine (FK_Ordine, FK_Prodotto, Quantita, PrezzoAcquisto) VALUES (2, 6, 1, 28.40), (2, 7, 1, 8.90);

-- --- 6. RECENSIONI E STORICO PUNTI ---
INSERT INTO Recensione (Voto, Testo, FK_Utente, FK_Prodotto) VALUES
(5, 'Efficace e veloce, non manca mai nel mio armadietto.', 2, 9),
(4, 'Ottima texture, lascia la pelle molto idratata.', 3, 6),
(5, 'Strumento preciso e facilissimo da usare.', 2, 18);

INSERT INTO StoricoPunti (Punti, Tipo, FK_Utente, FK_Ordine) VALUES
(1, 'ACCREDITO_ORDINE', 2, 1),
(-10, 'UTILIZZO_SCONTO', 3, 2),
(1, 'ACCREDITO_ORDINE', 3, 2);

-- --- 7. PERSISTENZA (CARRELLO E WISHLIST SALVATI) ---
-- Mario Rossi ha lasciato una Vitamina C nel carrello
INSERT INTO CarrelloPersistente (FK_Utente, FK_Prodotto, Quantita) VALUES (2, 1, 1);

-- Laura Bianchi ha il Misuratore Pressione nella Wishlist
INSERT INTO WishlistPersistente (FK_Utente, FK_Prodotto) VALUES (3, 18);

-- Giuseppe Verdi ha lo Spazzolino Elettrico nella Wishlist
INSERT INTO WishlistPersistente (FK_Utente, FK_Prodotto) VALUES (4, 16);