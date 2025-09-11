package com.petcare.back.repository;

import com.petcare.back.domain.entity.ServiceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface ServiceSessionRepository extends JpaRepository<ServiceSession, Long> {
    Optional<ServiceSession> findByBookingId(Long bookingId);
}
