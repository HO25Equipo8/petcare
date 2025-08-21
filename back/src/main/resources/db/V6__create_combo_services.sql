-- Crea la tabla de combos de servicios con descuentos
CREATE TABLE combo_services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,             -- ComboEnum
    description VARCHAR(255) NOT NULL,
    discount DOUBLE NOT NULL,              -- porcentaje
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Relación entre combos y servicios individuales
CREATE TABLE combo_service_services (
    combo_service_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    PRIMARY KEY (combo_service_id, service_id),
    FOREIGN KEY (combo_service_id) REFERENCES combo_services(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);
