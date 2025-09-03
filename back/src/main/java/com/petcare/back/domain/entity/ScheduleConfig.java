package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.WeekDayEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "schedule_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sitter_id", nullable = false)
    private User sitter;

    @Column(name = "configuration_name")
    private String configurationName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "scheduleConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleTurn> turns;

    @Column(name = "service_duration_minutes")
    private Integer serviceDurationMinutes;

    @Column(name = "interval_between_services")
    private Integer intervalBetweenServices;

    @Column(nullable = false)
    private Boolean active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


