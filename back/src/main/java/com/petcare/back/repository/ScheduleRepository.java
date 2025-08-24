package com.petcare.back.repository;

import com.petcare.back.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Modifying
    @Query("UPDATE Schedule s SET s.status = 'EXPIRADO' WHERE s.status = 'DISPONIBLE' AND s.establishedTime < :now")
    int expireSchedulesBefore(@Param("now") Instant now);

}
