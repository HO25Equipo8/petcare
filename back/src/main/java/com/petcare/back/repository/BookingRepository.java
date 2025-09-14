package com.petcare.back.repository;

import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT b FROM Booking b
            JOIN b.serviceItems i
            WHERE b.status = 'COMPLETED'
              AND (
                (b.owner.id = :authorId AND i.professional = :target)
                OR
                (b.owner.id = :targetId AND i.professional = :author)
              )
            """)
    List<Booking> findCompletedBookingsBetweenUsers(
            @Param("authorId") Long authorId,
            @Param("targetId") Long targetId,
            @Param("author") User author,
            @Param("target") User target
    );

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            JOIN b.serviceItems i
            WHERE b.owner.id = :ownerId
              AND i.professional.id = :sitterId
            """)
    int countByOwnerAndSitter(@Param("ownerId") Long ownerId, @Param("sitterId") Long sitterId);

    List<Booking> findByOwnerId(Long id);

    @Query("""
                SELECT DISTINCT b
                FROM Booking b
                JOIN b.serviceItems i
                WHERE i.professional = :user
            """)
    List<Booking> findByProfessional(@Param("user") User user);

    @Query("""
                SELECT s FROM Booking b JOIN b.schedules s
                WHERE b.owner.id = :ownerId
                  AND s.status = :status
            """)
    List<Schedule> findSchedulesByOwnerAndStatus(@Param("ownerId") Long ownerId,
                                                 @Param("status") ScheduleStatus status);

    @Query("""
                SELECT s FROM Booking b JOIN b.schedules s
                WHERE b.owner.id = :ownerId
                  AND FUNCTION('DAYOFWEEK', s.establishedTime) = :day
            """)
    List<Schedule> findSchedulesByOwnerAndDay(@Param("ownerId") Long ownerId,
                                              @Param("day") Integer day);

    @Query("""
                SELECT s FROM Booking b JOIN b.schedules s
                WHERE b.owner.id = :ownerId
                  AND s.status = :status
                  AND FUNCTION('DAYOFWEEK', s.establishedTime) = :day
            """)
    List<Schedule> findSchedulesByOwnerStatusAndDay(@Param("ownerId") Long ownerId,
                                                    @Param("status") ScheduleStatus status,
                                                    @Param("day") Integer day);

    @Query("""
                SELECT s FROM Booking b JOIN b.schedules s
                WHERE b.owner.id = :ownerId
                  AND s.establishedTime BETWEEN :from AND :to
            """)
    List<Schedule> findSchedulesByOwnerAndDateRange(@Param("ownerId") Long ownerId,
                                                    @Param("from") Instant from,
                                                    @Param("to") Instant to);

    @Query("""
    SELECT bsi.schedule FROM BookingServiceItem bsi
    WHERE bsi.booking.owner.id = :ownerId
      AND bsi.status = :status
""")
    List<Schedule> findSchedulesFromServiceItems(@Param("ownerId") Long ownerId,
                                                 @Param("status") BookingStatusEnum status);

}
