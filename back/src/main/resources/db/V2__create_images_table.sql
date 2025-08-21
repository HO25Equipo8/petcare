-- Crea la tabla de imágenes para perfil, verificación y mascotas
CREATE TABLE images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);
