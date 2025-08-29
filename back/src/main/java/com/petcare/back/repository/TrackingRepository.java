package com.petcare.back.repository;

import com.petcare.back.domain.entity.TrackingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.UUID;

public interface TrackingRepository extends JpaRepository<TrackingPoint, UUID> {

    @Query("SELECT t FROM TrackingPoint t WHERE t.bookingId = :bookingId ORDER BY t.timestamp DESC LIMIT 1")
    TrackingPoint findLastLocationByBookingId(UUID bookingId);
}
