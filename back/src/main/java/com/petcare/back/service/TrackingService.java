package com.petcare.back.service;

import com.petcare.back.domain.entity.TrackingPoint;
import com.petcare.back.repository.TrackingRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TrackingService {

    private final TrackingRepository repository;

    public TrackingService(TrackingRepository repository) {
        this.repository = repository;
    }

    public TrackingPoint getLastLocation(UUID bookingId) {
        return repository.findLastLocationByBookingId(bookingId);
    }

    public TrackingPoint savePoint(TrackingPoint point) {
        return repository.save(point);
    }
}
