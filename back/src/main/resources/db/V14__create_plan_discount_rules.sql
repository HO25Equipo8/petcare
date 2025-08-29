CREATE TABLE plan_discount_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    min_sessions_per_week DOUBLE NOT NULL,
    max_sessions_per_week DOUBLE NOT NULL,
    discount DECIMAL(10,2) NOT NULL
);