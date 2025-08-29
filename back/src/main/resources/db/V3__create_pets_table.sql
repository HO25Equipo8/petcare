-- Crea la tabla de mascotas con relación a usuarios e imágenes
CREATE TABLE pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),                  -- PetTypeEnum
    breed VARCHAR(100),
    age INT,
    weight DOUBLE,                     -- Peso en kg
    color VARCHAR(50),                 -- Color del pelaje
    pet_size VARCHAR(50),                  -- Tamaño: pequeño, mediano, grande
    birth_date DATE,                   -- Fecha de nacimiento
    microchip VARCHAR(100),           -- Número de microchip

    temperament VARCHAR(50),          -- TemperamentEnum
    vaccination VARCHAR(50),          -- VaccinationEnum
    health VARCHAR(50),               -- HealthStatusEnum

    allergies TEXT,                   -- Alergias conocidas
    medications TEXT,                 -- Medicamentos actuales
    special_needs TEXT,               -- Cuidados especiales, comportamiento, etc.
    emergency_contact VARCHAR(100),   -- Veterinario o contacto de emergencia

    active BOOLEAN DEFAULT TRUE,

    image_id BIGINT,                  -- FK a images(id)
    user_id BIGINT,                   -- FK a users(id)

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (image_id) REFERENCES images(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
