package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "booking_professionals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingProfessional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User professional;

    public BookingProfessional(Booking booking, User professional) {
        this.booking = booking;
        this.professional = professional;
    }
}