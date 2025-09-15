package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.WeekDayEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "schedule_turn")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTurn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_config_id")
    private ScheduleConfig scheduleConfig;

    @Enumerated(EnumType.STRING)
    @Column(name = "day", nullable = false)
    private WeekDayEnum day;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_start")
    private LocalTime breakStart;

    @Column(name = "break_end")
    private LocalTime breakEnd;
}
