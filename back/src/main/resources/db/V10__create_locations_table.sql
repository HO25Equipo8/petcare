-- Crea la tabla de ubicaciones físicas donde se ofrecen servicios
CREATE TABLE locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),                      -- Ej: "Sucursal San Lorenzo"
    street VARCHAR(150),
    number VARCHAR(10),                               -- Calle y número
    city VARCHAR(100),
    province VARCHAR(100),
    country VARCHAR(100),
    latitude DOUBLE,
    longitude DOUBLE
);
