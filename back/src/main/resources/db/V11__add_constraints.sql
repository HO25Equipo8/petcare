-- 🔗 Relaciones en users
ALTER TABLE users
ADD CONSTRAINT fk_users_photo_perfil
FOREIGN KEY (photo_perfil_id) REFERENCES images(id);

-- 🔗 Relaciones en pets
ALTER TABLE pets
ADD CONSTRAINT fk_pets_owner
FOREIGN KEY (user_id) REFERENCES users(id),
ADD CONSTRAINT fk_pets_image
FOREIGN KEY (image_id) REFERENCES images(id);

-- 🔗 Relaciones en bookings
ALTER TABLE bookings
ADD CONSTRAINT fk_bookings_owner
FOREIGN KEY (owner_id) REFERENCES users(id),
ADD CONSTRAINT fk_bookings_sitter
FOREIGN KEY (sitter_id) REFERENCES users(id),
ADD CONSTRAINT fk_bookings_pet
FOREIGN KEY (pet_id) REFERENCES pets(id),
ADD CONSTRAINT fk_bookings_service
FOREIGN KEY (service_id) REFERENCES services(id),
ADD CONSTRAINT fk_bookings_combo_service
FOREIGN KEY (combo_service_id) REFERENCES combo_services(id),
ADD CONSTRAINT fk_bookings_plan
FOREIGN KEY (plan_id) REFERENCES plans(id),
ADD CONSTRAINT fk_bookings_schedule
FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id);

-- 🔗 Relaciones en schedules
ALTER TABLE schedules
ADD CONSTRAINT fk_schedules_config
FOREIGN KEY (schedule_config_id) REFERENCES schedule_config(id);

-- 🔗 Relaciones en schedule_config
ALTER TABLE schedule_config
ADD CONSTRAINT fk_schedule_config_sitter
FOREIGN KEY (sitter_id) REFERENCES users(id);

-- 🔗 Relaciones en service_applicable_pet_types
ALTER TABLE service_applicable_pet_types
ADD CONSTRAINT fk_service_pet_type_service
FOREIGN KEY (service_id) REFERENCES services(id);

-- 🔗 Relaciones en combo_service_services
ALTER TABLE combo_service_services
ADD CONSTRAINT fk_combo_service
FOREIGN KEY (combo_service_id) REFERENCES combo_services(id),
ADD CONSTRAINT fk_combo_service_service
FOREIGN KEY (service_id) REFERENCES services(id);

-- 🔗 Relaciones en schedule_days
ALTER TABLE schedule_days
ADD CONSTRAINT fk_schedule_days_config
FOREIGN KEY (schedule_config_id) REFERENCES schedule_config(id);

-- 🔗 Relaciones en locations
-- Ya creada en V10, ahora conectamos:
ALTER TABLE schedules
ADD CONSTRAINT fk_schedules_location
FOREIGN KEY (location_id) REFERENCES locations(id);

ALTER TABLE services
ADD CONSTRAINT fk_services_location
FOREIGN KEY (location_id) REFERENCES locations(id);
