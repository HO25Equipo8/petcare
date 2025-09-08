package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.IncidentsTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Entity
@Getter
@Setter
public class Incidents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IncidentsTypes incidentsType;

    private String description;

    @Column(name = "Incidents_date", nullable = false)
    private Instant incidentsDate = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

}