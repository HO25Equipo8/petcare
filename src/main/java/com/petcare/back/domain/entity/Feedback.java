package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.FeedbackContext;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "feedbacks")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User author; // quien deja el feedback

    @ManyToOne
    private User target; // el usuario que lo recibe

    private String comment;

    @Min(1)
    @Max(5)
    private Integer rating; // ⭐ de 1 a 5

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private FeedbackContext context;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}

