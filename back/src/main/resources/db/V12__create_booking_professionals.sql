CREATE TABLE booking_professionals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    booking_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role_professional VARCHAR(50) NOT NULL, -- Enum: PASEADOR, CUIDADOR, etc.

    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
