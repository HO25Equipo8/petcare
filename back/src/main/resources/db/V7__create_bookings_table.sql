-- Tabla principal de reservas entre dueños, mascotas y servicios
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    owner_id BIGINT NOT NULL,           -- FK a users(id)
    pet_id BIGINT NOT NULL,             -- FK a pets(id)

    offering_id BIGINT,                 -- FK a services(id)
    combo_offering_id BIGINT,           -- FK a combo_services(id)
    plan_id BIGINT,                     -- FK a plans(id)

    reservation_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,        -- BookingStatusEnum
    final_price DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (owner_id) REFERENCES users(id),
    FOREIGN KEY (pet_id) REFERENCES pets(id),
    FOREIGN KEY (offering_id) REFERENCES services(id),
    FOREIGN KEY (combo_offering_id) REFERENCES combo_services(id),
    FOREIGN KEY (plan_id) REFERENCES plans(id)
);
