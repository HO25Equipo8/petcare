-- Crea la tabla de servicios disponibles para mascotas
CREATE TABLE services (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,             -- ServicePetsEnum
    description VARCHAR(255) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Mapea los tipos de mascota aplicables a cada servicio
CREATE TABLE service_applicable_pet_types (
    service_id BIGINT NOT NULL,
    pet_type VARCHAR(50) NOT NULL,         -- PetTypeEnum
    PRIMARY KEY (service_id, pet_type),
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);
