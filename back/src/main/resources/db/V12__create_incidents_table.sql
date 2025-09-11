create table incidents(
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          incidents_type VARCHAR(50) NOT NULL,
                          description TEXT,
                          incidents_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);