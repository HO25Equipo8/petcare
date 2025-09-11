package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToMany
    @JoinTable(
            name = "booking_professionals",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> professionals = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "offering_id")
    private Offering offering;

    @ManyToOne
    @JoinColumn(name = "combo_offering_id")
    private ComboOffering comboOffering;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    private Incidents incidents;


    @Column(name = "reservation_date", nullable = false)
    private Instant reservationDate = Instant.now();

    @ManyToMany
    @JoinTable(
            name = "booking_schedules",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<Schedule> schedules = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatusEnum status;

    @Column(name = "final_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal finalPrice;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private ServiceSession serviceSession;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

