package com.petcare.back.service;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.response.BookingResponseDTO;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.BookingCreateMapper;
import com.petcare.back.domain.mapper.response.BookingResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final OfferingRepository offeringRepository;
    private final ComboOfferingRepository comboRepository;
    private final PlanRepository planRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository professionalRepository;
    private final BookingResponseMapper mapper;
    private final BookingCreateMapper bookingCreateMapper;

    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO dto) throws MyException {

        // 1. Autenticación y validación de rol
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER) {
            throw new MyException("Solo los dueños pueden seleccionar su plan");
        }

        // 2. Cargar owner y mascota
        User owner = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        Pet pet = petRepository.findById(dto.petId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // 3. Crear booking
        Booking booking = bookingCreateMapper.toEntity(dto);
        booking.setOwner(owner);
        booking.setPet(pet);

        // 4. Asignar offering, combo y plan
        if (dto.offeringId() != null) {
            booking.setOffering(offeringRepository.findById(dto.offeringId()).orElse(null));
        }
        if (dto.comboOfferingId() != null) {
            booking.setComboOffering(comboRepository.findById(dto.comboOfferingId()).orElse(null));
        }
        if (dto.planId() != null) {
            booking.setPlan(planRepository.findById(dto.planId()).orElse(null));
        }

        // 5. Asignar horarios
        booking.setSchedules(scheduleRepository.findAllById(dto.scheduleIds()));
        booking.setReservationDate(Instant.now());
        booking.setStatus(BookingStatusEnum.PENDIENTE);

        // 6. Asignar profesionales
        List<BookingProfessional> bookingProfessionals = dto.professionals().stream()
                .map(p -> {
                    User prof = professionalRepository.findById(p.userId())
                            .orElseThrow(() -> new RuntimeException("Professional not found: " + p.userId()));
                    return new BookingProfessional(booking, prof);
                }).toList();
        booking.setProfessionals(bookingProfessionals);

        // 7. Calcular precio final con descuento del plan
        booking.setFinalPrice(calculateBookingPrice(booking));

        // 8. Guardar reserva y retornar DTO
        bookingRepository.save(booking);
        return mapper.toDTO(booking);
    }

    public BigDecimal calculateBookingPrice(Booking booking) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 1. Precio base del servicio individual
        if (booking.getOffering() != null && booking.getOffering().getBasePrice() != null) {
            totalPrice = totalPrice.add(booking.getOffering().getBasePrice());
        }

        // 2. Precio del combo con su descuento propio
        if (booking.getComboOffering() != null && booking.getComboOffering().getOfferings() != null) {
            BigDecimal comboTotal = booking.getComboOffering().getOfferings().stream()
                    .map(Offering::getBasePrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            double comboDiscount = booking.getComboOffering().getDiscount(); // ej. 0.1 = 10%
            BigDecimal discountAmount = comboTotal.multiply(BigDecimal.valueOf(comboDiscount));
            totalPrice = totalPrice.add(comboTotal.subtract(discountAmount));
        }

        // 3. Aplicar promoción del plan (ej. 0.2 = 20%)
        if (booking.getPlan() != null && booking.getPlan().getPromotion() != null && totalPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal promotionAmount = totalPrice.multiply(BigDecimal.valueOf(booking.getPlan().getPromotion()));
            totalPrice = totalPrice.subtract(promotionAmount);
        }
        return totalPrice.max(BigDecimal.ZERO); // nunca precio negativo
    }
}

