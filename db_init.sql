
create database if not exists mensadb;
use mensadb;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cf VARCHAR(16) NOT NULL UNIQUE,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password CHAR(64) NOT NULL,
    status ENUM('studente', 'lavoratore', 'docente', 'dottorando') NOT NULL,
    telefono VARCHAR(20),
    credito FLOAT DEFAULT 0,
    tmp_code CHAR(32) NOT NULL DEFAULT 'new'
);

CREATE TABLE IF NOT EXISTS transazioni (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    importo FLOAT NOT NULL,
    datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    modalita ENUM('ricarica con carta di credito', 'ricarica in contanti', 'pagamento alla mensa', 'debug') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


INSERT INTO users (cf, nome, cognome, email, password, status, telefono, credito)
VALUES
('RSSMRA85M01H501Z', 'Mario', 'Rossi', 'm.r', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'studente', '1234567890', 0),
('VRDLGI92A01F205Z', 'Luigi', 'Verdi', 'l.v', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'docente', '0987654321', 0),
('BNCLRA80M01E204Q', 'Lara', 'Bianchi', 'l.b', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'lavoratore', '3456781234', 0),
('DTTFRN90A01C123H', 'Franco', 'Dotti', 'f.d', 'ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb', 'dottorando', '9876543210', 0);


INSERT INTO transazioni (user_id, importo, datetime, modalita)
VALUES
(1, 20.00, '2024-10-01 12:30:00', 'ricarica con carta di credito'),
(2, -15.00, '2024-10-02 13:00:00', 'pagamento alla mensa'),
(1, -5.00, '2024-10-03 14:00:00', 'pagamento alla mensa'),
(3, 50.00, '2024-10-04 11:15:00', 'ricarica in contanti'),
(4, -10.00, '2024-10-05 12:45:00', 'pagamento alla mensa');


DROP TRIGGER IF EXISTS update_credito_after_transaction;
CREATE TRIGGER update_credito_after_transaction
AFTER INSERT ON transazioni
FOR EACH ROW
BEGIN
    -- Aggiorna il credito dell'utente basato sull'importo della transazione
    UPDATE users
    SET credito = credito + NEW.importo
    WHERE id = NEW.user_id;
END;


DROP TRIGGER IF EXISTS prevent_negative_credit;
CREATE TRIGGER prevent_negative_credit
BEFORE INSERT ON transazioni
FOR EACH ROW
BEGIN
    DECLARE nuovo_credito FLOAT;

    -- Calcola il nuovo credito dopo la transazione
    SELECT credito + NEW.importo INTO nuovo_credito
    FROM users
    WHERE id = NEW.user_id;

    -- Se il nuovo credito sarebbe negativo, lancia un errore
    IF nuovo_credito < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Transazione fallita: credito insufficiente';
    END IF;
END

