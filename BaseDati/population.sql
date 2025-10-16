USE pharmaweb_db;

-- ==========================================================================================
-- --- Popolamento Tabelle Primarie (senza dipendenze) ---
-- ==========================================================================================

-- Popolamento Categoria
INSERT INTO Categoria (Nome) VALUES
('Integratori e Vitamine'),          -- ID 1
('Cosmetica e Bellezza'),            -- ID 2
('Farmaci da Banco'),                -- ID 3
('Infanzia e Bambini'),              -- ID 4
('Igiene e Cura Personale'),         -- ID 5
('Dispositivi Medici');              -- ID 6

-- Popolamento Utente
INSERT INTO Utente (Email, Password, Nome, Cognome, Ruolo, PuntiFedelta) VALUES
('admin@pharmaweb.it', 'hash_password_admin', 'Admin', 'Pharma', 'admin', 0),                                 -- ID 1
('mario.rossi@email.com', 'hash_password_cliente1', 'Mario', 'Rossi', 'cliente', 150),                        -- ID 2
('laura.bianchi@email.com', 'hash_password_cliente2', 'Laura', 'Bianchi', 'cliente', 250),                      -- ID 3
('giuseppe.verdi@email.com', 'hash_password_cliente3', 'Giuseppe', 'Verdi', 'cliente', 25),                   -- ID 4
('anna.neri@email.com', 'hash_password_cliente4', 'Anna', 'Neri', 'cliente', 80);                             -- ID 5

-- ==========================================================================================
-- --- Popolamento Tabelle Dipendenti da Utente ---
-- ==========================================================================================

-- Popolamento IndirizzoSpedizione
INSERT INTO IndirizzoSpedizione (NomeDestinatario, Via, Citta, CAP, Provincia, FK_Utente) VALUES
('Mario Rossi - Casa', 'Via Roma 1', 'Napoli', '80121', 'NA', 2),
('Mario Rossi - Ufficio', 'Corso Umberto I 25', 'Napoli', '80138', 'NA', 2),
('Laura Bianchi', 'Piazza del Popolo 10', 'Roma', '00187', 'RM', 3),
('Giuseppe Verdi', 'Via Dante Alighieri 5', 'Milano', '20121', 'MI', 4),
('Anna Neri', 'Viale dei Mille 15', 'Firenze', '50131', 'FI', 5);

-- Popolamento MetodoPagamento
INSERT INTO MetodoPagamento (TipoCarta, Titolare, Ultime4Cifre, MeseScadenza, AnnoScadenza, FK_Utente) VALUES
('Visa', 'Mario Rossi', '1234', 12, 2026, 2),
('Mastercard', 'Laura Bianchi', '5678', 6, 2027, 3),
('Paypal', 'Giuseppe Verdi', '9012', 3, 2028, 4),
('Visa', 'Anna Neri', '3456', 9, 2025, 5);

-- ==========================================================================================
-- --- Popolamento Catalogo Prodotti ---
-- ==========================================================================================

INSERT INTO Prodotto (Nome, Descrizione, Prezzo, ScontoPercentuale, QuantitaDisponibile, URL_Immagine, FK_Categoria) VALUES
-- Integratori (Cat ID 1)
('Vitamina C 1000mg', 'Integratore di Vitamina C, 30 compresse effervescenti.', 12.00, 10, 80, 'img/vitamina-c.jpg', 1),
('Magnesio Supremo', 'Integratore di magnesio in polvere, 150g.', 15.50, 0, 60, 'img/magnesio-supremo.jpg', 1),
('Omega 3 Extra', 'Integratore di acidi grassi Omega 3, 60 perle.', 25.00, 15, 45, 'img/omega-3.jpg', 1),
-- Cosmetica (Cat ID 2)
('Crema Viso Idratante', 'Crema idratante per pelli secche, 50ml.', 22.99, 0, 50, 'img/crema-viso.jpg', 2),
('Siero Anti-età', 'Siero concentrato con acido ialuronico, 30ml.', 35.50, 20, 30, 'img/siero-antieta.jpg', 2),
('Shampoo Delicato', 'Shampoo per uso frequente, 250ml.', 8.90, 0, 120, 'img/shampoo.jpg', 2),
-- Farmaci da Banco (Cat ID 3)
('Paracetamolo 500mg', '20 compresse per stati febbrili e dolorosi.', 7.50, 0, 100, 'img/paracetamolo.jpg', 3),
('Ibuprofene 400mg', '12 compresse per dolori di varia natura.', 9.20, 10, 90, 'img/ibuprofene.jpg', 3),
-- Infanzia (Cat ID 4)
('Biberon Neonato', 'Biberon anti-colica per neonati 0-6 mesi.', 9.90, 0, 40, 'img/biberon.jpg', 4),
('Pasta Protettiva', 'Pasta lenitiva per il cambio pannolino, 100g.', 6.50, 0, 70, 'img/pasta-protettiva.jpg', 4),
-- Igiene (Cat ID 5)
('Collutorio Antibatterico', 'Collutorio per ligiene orale, 500ml.', 5.80, 5, 150, 'img/collutorio.jpg', 5),
('Spazzolino Elettrico', 'Spazzolino elettrico con testina di ricambio.', 45.00, 0, 25, 'img/spazzolino.jpg', 5),
-- Dispositivi Medici (Cat ID 6)
('Termometro Digitale', 'Termometro a misurazione rapida.', 11.50, 0, 60, 'img/termometro.jpg', 6),
('Cerotti Assortiti', 'Confezione da 40 cerotti di varie misure.', 4.20, 0, 200, 'img/cerotti.jpg', 6),
('Misuratore Pressione', 'Misuratore di pressione da braccio automatico.', 39.90, 10, 35, 'img/misuratore-pressione.jpg', 6);

-- ==========================================================================================
-- --- Popolamento Ordini e Dettagli ---
-- ==========================================================================================

-- Ordine 1: Mario Rossi (FK_Utente = 2)
INSERT INTO Ordine (ImportoTotale, IndirizzoSpedizione, MetodoPagamentoUtilizzato, Stato, PuntiGuadagnati, PuntiUtilizzati, FK_Utente) VALUES
(27.30, 'Mario Rossi - Casa, Via Roma 1, Napoli, 80121, NA', 'Visa terminante in 1234', 'Consegnato', 1, 0, 2);
INSERT INTO RigaOrdine (FK_Ordine, FK_Prodotto, Quantita, PrezzoAcquisto) VALUES
(1, 1, 2, 7.50),  -- 2x Paracetamolo (prezzo pieno 7.50)
(1, 3, 1, 12.30); -- 1x Omega 3 (prezzo scontato 15%)

-- Ordine 2: Laura Bianchi (FK_Utente = 3) - Usa punti per uno sconto
-- Subtotale: 1x Siero (28.40) + 1x Shampoo (8.90) = 37.30. Usa 10 punti (10 euro sconto). Totale: 27.30
INSERT INTO Ordine (ImportoTotale, IndirizzoSpedizione, MetodoPagamentoUtilizzato, Stato, PuntiGuadagnati, PuntiUtilizzati, FK_Utente) VALUES
(27.30, 'Laura Bianchi, Piazza del Popolo 10, Roma, 00187, RM', 'Mastercard terminante in 5678', 'Spedito', 1, 10, 3);
INSERT INTO RigaOrdine (FK_Ordine, FK_Prodotto, Quantita, PrezzoAcquisto) VALUES
(2, 5, 1, 28.40), -- 1x Siero (prezzo scontato 20%)
(2, 6, 1, 8.90);  -- 1x Shampoo (prezzo pieno 8.90)

-- Ordine 3: Giuseppe Verdi (FK_Utente = 4)
INSERT INTO Ordine (ImportoTotale, IndirizzoSpedizione, MetodoPagamentoUtilizzato, Stato, PuntiGuadagnati, PuntiUtilizzati, FK_Utente) VALUES
(42.40, 'Giuseppe Verdi, Via Dante Alighieri 5, Milano, 20121, MI', 'Paypal', 'In elaborazione', 2, 0, 4);
INSERT INTO RigaOrdine (FK_Ordine, FK_Prodotto, Quantita, PrezzoAcquisto) VALUES
(3, 10, 1, 6.50), -- 1x Pasta Protettiva
(3, 15, 1, 35.91); -- 1x Misuratore Pressione (scontato 10%)

-- Ordine 4: Mario Rossi (FK_Utente = 2) - Secondo ordine
INSERT INTO Ordine (ImportoTotale, IndirizzoSpedizione, MetodoPagamentoUtilizzato, Stato, PuntiGuadagnati, PuntiUtilizzati, FK_Utente) VALUES
(45.00, 'Mario Rossi - Ufficio, Corso Umberto I 25, Napoli, 80138, NA', 'Visa terminante in 1234', 'Consegnato', 2, 0, 2);
INSERT INTO RigaOrdine (FK_Ordine, FK_Prodotto, Quantita, PrezzoAcquisto) VALUES
(4, 12, 1, 45.00); -- 1x Spazzolino Elettrico

-- ==========================================================================================
-- --- Popolamento Tabelle Secondarie (Recensioni, Storico Punti) ---
-- ==========================================================================================

-- Popolamento Recensione (basato su prodotti acquistati negli ordini di esempio)
INSERT INTO Recensione (Voto, Testo, FK_Utente, FK_Prodotto) VALUES
(5, 'Ottimo prodotto, efficace e conveniente. Spedizione velocissima!', 2, 1), -- Mario Rossi recensisce Paracetamolo
(4, 'Lascia la pelle molto morbida, anche se la profumazione è un po forte. Consigliato.', 3, 5), -- Laura Bianchi recensisce Siero
(5, 'Il miglior spazzolino elettrico che abbia mai provato. Vale ogni centesimo!', 2, 12); -- Mario Rossi recensisce Spazzolino Elettrico

-- Popolamento StoricoPunti
INSERT INTO StoricoPunti (Punti, Tipo, FK_Utente, FK_Ordine) VALUES
-- Punti Ordine 1
(1, 'ACCREDITO_ORDINE', 2, 1),
-- Punti Ordine 2
(-10, 'UTILIZZO_SCONTO', 3, 2),
(1, 'ACCREDITO_ORDINE', 3, 2),
-- Punti Ordine 3
(2, 'ACCREDITO_ORDINE', 4, 3),
-- Punti Ordine 4
(2, 'ACCREDITO_ORDINE', 2, 4),
-- Movimento manuale (es. bonus)
(50, 'BONUS_BENVENUTO', 5, NULL);