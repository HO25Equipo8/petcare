-- Crea la tabla de planes con frecuencia, duración e intensidad
CREATE TABLE plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    times_per_week INT NOT NULL,               -- Cantidad de sesiones por semana
    interval_enum VARCHAR(20) NOT NULL,             -- IntervalEnum: SEMANAL, QUINCENAL, MENSUAL
    promotion DOUBLE NOT NULL,                 -- Porcentaje de descuento aplicado
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
