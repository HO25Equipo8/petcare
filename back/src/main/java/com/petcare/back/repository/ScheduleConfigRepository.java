package com.petcare.back.repository;

import com.petcare.back.domain.entity.ScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleConfigRepository extends JpaRepository<ScheduleConfig, Long> {
}
