-- Crea la tabla de combos de servicios con descuentos
CREATE TABLE combo_offerings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,             -- ComboEnum
    description VARCHAR(255) NOT NULL,
    discount DOUBLE NOT NULL,              -- porcentaje
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Relación entre combos y servicios individuales
CREATE TABLE combo_offering_services (
    combo_offering_id BIGINT NOT NULL,
    offering_id BIGINT NOT NULL,
    PRIMARY KEY (combo_offering_id, offering_id),
    FOREIGN KEY (combo_offering_id) REFERENCES combo_offerings(id) ON DELETE CASCADE,
    FOREIGN KEY (offering_id) REFERENCES offerings(id) ON DELETE CASCADE
);
