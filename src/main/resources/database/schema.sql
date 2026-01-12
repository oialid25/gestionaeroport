CREATE TABLE IF NOT EXISTS vol (
    id_vol INTEGER PRIMARY KEY AUTO_INCREMENT,
    destination VARCHAR(100) NOT NULL,
    depart VARCHAR(100) NOT NULL,
    date_vol DATE NOT NULL,
    duree_vol INTEGER NOT NULL,
    seat_occupee INTEGER DEFAULT 0,
    id_avion VARCHAR(50),
    FOREIGN KEY (id_avion) REFERENCES avion(matricule)
);
CREATE TABLE IF NOT EXISTS passager (
    num_passeport VARCHAR(50) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    genre VARCHAR(10),
    date_naissance DATE NOT NULL,
    masse_bag DECIMAL(10,2) NOT NULL,
    id_vol INTEGER NOT NULL,
    FOREIGN KEY (id_vol) REFERENCES vol(id_vol) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS avion (
    matricule VARCHAR(50) PRIMARY KEY,
    dimension_w DECIMAL(10,2),
    dimension_h DECIMAL(10,2),
    masse_supportee DECIMAL(10,2),
    seat_capacite INTEGER,
    etat VARCHAR(20)
);
CREATE INDEX  idx_passager_vol ON passager(id_vol);
CREATE INDEX idx_vol_date ON vol(date_vol);
