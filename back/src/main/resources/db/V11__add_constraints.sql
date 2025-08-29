-- 🔗 Relaciones en users
ALTER TABLE users
ADD CONSTRAINT fk_users_profile_photo
FOREIGN KEY (profile_photo_id) REFERENCES images(id);

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
FOREIGN KEY (offering_id) REFERENCES offerings(id),
ADD CONSTRAINT fk_bookings_combo_service
FOREIGN KEY (combo_offering_id) REFERENCES combo_offerings(id),
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

-- 🔗 Relaciones en offering_applicable_pet_types
ALTER TABLE offering_applicable_pet_types
ADD CONSTRAINT fk_offering_pet_type_service
FOREIGN KEY (offering_id) REFERENCES offerings(id);

-- 🔗 Relaciones en combo_offering_services
ALTER TABLE combo_offering_services
ADD CONSTRAINT fk_combo_offering
FOREIGN KEY (combo_offering_id) REFERENCES combo_offerings(id),
ADD CONSTRAINT fk_combo_offering_service
FOREIGN KEY (offering_id) REFERENCES offerings(id);

-- 🔗 Relaciones en schedule_days
ALTER TABLE schedule_days
ADD CONSTRAINT fk_schedule_days_config
FOREIGN KEY (schedule_config_id) REFERENCES schedule_config(id);

-- 🔗 Relaciones en locations
-- Ya creada en V10, ahora conectamos:
ALTER TABLE schedules
ADD CONSTRAINT fk_schedules_location
FOREIGN KEY (location_id) REFERENCES locations(id);

