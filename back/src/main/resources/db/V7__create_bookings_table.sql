-- Crea la tabla de reservas entre dueños, cuidadores, mascotas y servicios
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    owner_id BIGINT NOT NULL,         -- FK a users(id)
    sitter_id BIGINT NOT NULL,        -- FK a users(id)
    pet_id BIGINT NOT NULL,           -- FK a pets(id)

    service_id BIGINT,                -- FK a services(id)
    combo_service_id BIGINT,          -- FK a combo_services(id)
    plan_id BIGINT,                   -- FK a plans(id)
    schedule_id BIGINT NOT NULL,      -- FK a schedules(schedule_id)

    reservation_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,      -- BookingStatusEnum
    final_price DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);