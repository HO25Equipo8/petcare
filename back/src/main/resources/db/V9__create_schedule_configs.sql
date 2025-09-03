-- Tabla principal de configuraciones de disponibilidad
CREATE TABLE schedule_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sitter_id BIGINT NOT NULL,                         -- FK a users(id)
    configuration_name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    service_duration_minutes INT,
    interval_between_services INT,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sitter_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Tabla de turnos por día, asociados a una configuración
CREATE TABLE schedule_turn (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    schedule_config_id BIGINT NOT NULL,                -- FK a schedule_config(id)
    day VARCHAR(20) NOT NULL,                          -- WeekDayEnum
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    break_start TIME,
    break_end TIME,
    FOREIGN KEY (schedule_config_id) REFERENCES schedule_config(id) ON DELETE CASCADE
);
