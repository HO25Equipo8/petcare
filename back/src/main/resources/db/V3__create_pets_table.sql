-- Crea la tabla de mascotas con relación a usuarios e imágenes
CREATE TABLE pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),              -- PetTypeEnum
    breed VARCHAR(100),
    age INT,
    temperament VARCHAR(50),       -- TemperamentEnum
    vaccination VARCHAR(50),       -- VaccinationEnum
    health VARCHAR(50),            -- HealthStatusEnum
    active BOOLEAN DEFAULT TRUE,
    image_id BIGINT,               -- FK a images(id)
    user_id BIGINT,                -- FK a users(id)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
