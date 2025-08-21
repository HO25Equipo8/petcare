-- Crea la tabla de configuraciones de disponibilidad para cuidadores
CREATE TABLE schedule_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sitter_id BIGINT NOT NULL,                         -- FK a users(id)
    configuration_name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    break_start TIME,
    break_end TIME,
    service_duration_minutes INT,
    interval_between_services INT,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- Días activos de la configuración (lunes a domingo)
CREATE TABLE schedule_days (
    schedule_config_id BIGINT NOT NULL,
    day VARCHAR(20) NOT NULL,                          -- WeekDayEnum
    PRIMARY KEY (schedule_config_id, day),
    FOREIGN KEY (schedule_config_id) REFERENCES schedule_config(id) ON DELETE CASCADE
);
