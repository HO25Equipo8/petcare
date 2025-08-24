-- Crea la tabla de servicios disponibles para mascotas
CREATE TABLE offerings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,             -- ServicePetsEnum
    description VARCHAR(255) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Mapea los tipos de mascota aplicables a cada servicio
CREATE TABLE offering_applicable_pet_types (
    offering_id BIGINT NOT NULL,
    pet_type VARCHAR(50) NOT NULL,         -- PetTypeEnum
    PRIMARY KEY (offering_id, pet_type),
    FOREIGN KEY (offering_id) REFERENCES offerings(id) ON DELETE CASCADE
);
