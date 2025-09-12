package com.petcare.back.repository;

import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
    SELECT b FROM Booking b
    WHERE b.status = 'COMPLETED'
      AND (
        (b.owner.id = :authorId AND :target MEMBER OF b.professionals)
        OR
        (b.owner.id = :targetId AND :author MEMBER OF b.professionals)
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
    JOIN b.professionals p 
    WHERE b.owner.id = :ownerId 
      AND p.id = :sitterId
""")
    int countByOwnerAndSitter(@Param("ownerId") Long ownerId, @Param("sitterId") Long sitterId);

    List<Booking> findByOwnerId(Long id);

    List<Booking> findByProfessionalsContaining(User user);
}
