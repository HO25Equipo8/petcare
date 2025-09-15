package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "established_time", nullable = false)
    private Instant establishedTime;

    @ManyToOne
    @JoinColumn(name = "schedule_config_id", nullable = false)
    private ScheduleConfig scheduleConfig;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduleStatus status = ScheduleStatus.DISPONIBLE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

