CREATE TABLE incident_images (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 image_name VARCHAR(255),
                                 image_type VARCHAR(100),
                                 data LONGBLOB,
                                 incident_id BIGINT NOT NULL,
                                 CONSTRAINT fk_image_incident FOREIGN KEY (incident_id) REFERENCES incidents(id) ON DELETE CASCADE