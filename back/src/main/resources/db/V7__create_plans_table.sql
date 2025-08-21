-- Crea la tabla de planes con frecuencia, precio y promoción
CREATE TABLE plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,              -- FrequencyEnum
    base_price DECIMAL(10,2) NOT NULL,
    promotion DOUBLE NOT NULL,              -- porcentaje
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
