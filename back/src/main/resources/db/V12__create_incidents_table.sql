create table incidents(
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          incidents_type VARCHAR(50) NOT NULL,
                          description TEXT,
                          owner_id BIGINT NOT NULL,
                          pet_id BIGINT NOT NULL,
                          sitter_id BIGINT NOT NULL,
                          incidents_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_incidents_owner FOREIGN KEY (owner_id) REFERENCES users(id),
                          CONSTRAINT fk_incidents_pet FOREIGN KEY (pet_id) REFERENCES pets(id),
                          CONSTRAINT fk_incidents_sitter FOREIGN KEY (sitter_id) REFERENCES users(id)

);