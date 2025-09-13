package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_service_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(optional = false)
    @JoinColumn(name = "offering_id")
    private Offering offering;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id")
    private User professional;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatusEnum status;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
}
