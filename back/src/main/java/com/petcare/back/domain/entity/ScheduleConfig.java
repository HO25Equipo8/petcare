package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.WeekDayEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "schedule_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sitter_id")
    private User sitter;

    private String configurationName;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_start")
    private LocalTime breakStart;

    @Column(name = "break_end")
    private LocalTime breakEnd;

    @Column(name = "service_duration_minutes")
    private Integer serviceDurationMinutes;

    @Column(name = "interval_between_services")
    private Integer intervalBetweenServices;

    @Column(nullable = false)
    private Boolean active;

    @ElementCollection(targetClass = WeekDayEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "schedule_days", joinColumns = @JoinColumn(name = "schedule_config_id"))
    @Column(name = "day")
    private List<WeekDayEnum> days;
}

