package com.petcare.back.repository;

import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 🔧 Utilidades generales
    @Modifying
    @Query("""
        UPDATE Schedule s
        SET s.status = 'EXPIRADO'
        WHERE s.status = 'DISPONIBLE'
          AND s.establishedTime < :now
    """)
    int expireSchedulesBefore(@Param("now") Instant now);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.status = 'EXPIRADO'
          AND s.scheduleId NOT IN (
              SELECT bs.scheduleId FROM Booking b JOIN b.schedules bs
          )
    """)
    List<Schedule> findExpiredWithoutBookings();

    @Query("SELECT COUNT(s) FROM Schedule s WHERE s.scheduleConfig = :config")
    int countByScheduleConfig(@Param("config") ScheduleConfig config);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.id = :configId
          AND s.establishedTime > CURRENT_TIMESTAMP
          AND s.status = 'DISPONIBLE'
    """)
    List<Schedule> findFutureUnlinkedByConfig(@Param("configId") Long configId);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.id = :configId
          AND s.establishedTime > CURRENT_TIMESTAMP
          AND s.status = 'BLOQUEADO'
    """)
    List<Schedule> findFutureBlockedByConfig(@Param("configId") Long configId);

    // 🔹 Filtros por SITTER
    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.sitter.id = :sitterId
          AND s.status = :status
    """)
    Page<Schedule> findBySitterAndStatus(@Param("sitterId") Long sitterId, @Param("status") ScheduleStatus status, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.sitter.id = :sitterId
          AND FUNCTION('DAYOFWEEK', s.establishedTime) = :day
    """)
   Page<Schedule> findBySitterAndDay(@Param("sitterId") Long sitterId, @Param("day") Integer day, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.sitter.id = :sitterId
          AND s.status = :status
          AND FUNCTION('DAYOFWEEK', s.establishedTime) = :day
    """)
    Page<Schedule> findBySitterAndStatusAndDay(@Param("sitterId") Long sitterId, @Param("status") ScheduleStatus status, @Param("day") Integer day, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.sitter.id = :sitterId
          AND s.scheduleConfig.id = :configId
    """)
    Page<Schedule> findBySitterAndConfig(@Param("sitterId") Long sitterId, @Param("configId") Long configId, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.sitter.id = :sitterId
          AND s.establishedTime BETWEEN :from AND :to
    """)
    Page<Schedule> findBySitterAndDateRange(@Param("sitterId") Long sitterId, @Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    // 🔹 Filtros por ADMIN
    @Query("""
        SELECT s FROM Schedule s
        WHERE s.status = :status
    """)
    Page<Schedule> findByStatus(@Param("status") ScheduleStatus status, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE FUNCTION('DAYOFWEEK', s.establishedTime) = :day
    """)
    Page<Schedule> findByDay(@Param("day") Integer day, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.status = :status
          AND FUNCTION('DAYOFWEEK', s.establishedTime) = :day
    """)
    Page<Schedule> findByStatusAndDay(@Param("status") ScheduleStatus status, @Param("day") Integer day, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.scheduleConfig.id = :configId
    """)
    Page<Schedule> findByConfig(@Param("configId") Long configId, Pageable pageable);

    @Query("""
        SELECT s FROM Schedule s
        WHERE s.establishedTime BETWEEN :from AND :to
    """)
    Page<Schedule> findByDateRange(@Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @Query("""
    SELECT s FROM Schedule s
    WHERE s.scheduleConfig.sitter.id = :sitterId
""")
    Page<Schedule> findBySitterId(@Param("sitterId") Long sitterId, Pageable pageable);

    @Query("""
        SELECT s 
        FROM Schedule s
        WHERE s.scheduleConfig.sitter.id = :sitterId
          AND s.status = ScheduleStatus.DISPONIBLE
        ORDER BY s.establishedTime ASC
    """)
    List<Schedule> findAvailableBySitter(@Param("sitterId") Long sitterId);
}
