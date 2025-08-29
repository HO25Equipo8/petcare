-- Relación ManyToMany entre bookings y schedules
CREATE TABLE booking_schedules (
    booking_id BIGINT NOT NULL,
    schedule_id BIGINT NOT NULL,

    PRIMARY KEY (booking_id, schedule_id),
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id) ON DELETE CASCADE
);
