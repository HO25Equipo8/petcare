-- Crea la tabla de usuarios con dirección embebida, foto de perfil y rol
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    verified BOOLEAN DEFAULT FALSE,
    profile_photo_id BIGINT,
    role VARCHAR(50),

    -- Campos embebidos de Address
    address_street VARCHAR(100),
    address_number VARCHAR(20),
    address_city VARCHAR(100),
    address_state VARCHAR(100),
    address_code VARCHAR(20),
    address_latitude DOUBLE,
    address_longitude DOUBLE
);
