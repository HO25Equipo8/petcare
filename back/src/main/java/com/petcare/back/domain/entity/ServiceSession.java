package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.ServiceSessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceSessionStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "serviceSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpdateService> updates = new ArrayList<>();

    @OneToOne(mappedBy = "serviceSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Incidents incident;
}
