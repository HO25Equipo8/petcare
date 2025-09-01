package com.petcare.back.repository;

import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleConfigRepository extends JpaRepository<ScheduleConfig, Long> {
    boolean existsBySitterAndActiveTrue(User sitter);

    List<ScheduleConfig> findByActiveTrueAndEndDateBefore(LocalDate date);

    Optional<ScheduleConfig> findBySitterAndActiveTrue(User sitter);

    @Query("""
    SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
    FROM ScheduleConfig c
    WHERE c.sitter = :sitter
      AND c.active = true
      AND c.startDate <= :endDate
      AND c.endDate >= :startDate
""")
    boolean existsOverlappingConfig(
            @Param("sitter") User sitter,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
