package com.petcare.back.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tracking_points")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingPoint {

    @Id
    @GeneratedValue
    private UUID id;

    private Double latitude;
    private Double longitude;

    private LocalDateTime timestamp;

    @Column(name = "booking_id")
    private UUID bookingId; // referencia a la reserva
}
