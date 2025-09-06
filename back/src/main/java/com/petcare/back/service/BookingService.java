package com.petcare.back.service;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingSimulationRequestDTO;
import com.petcare.back.domain.dto.response.BookingResponseDTO;
import com.petcare.back.domain.dto.response.BookingSimulationResponseDTO;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.domain.mapper.request.BookingCreateMapper;
import com.petcare.back.domain.mapper.response.BookingResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.*;
import com.petcare.back.validation.ValidationBooking;
import com.petcare.back.validation.ValidationCombo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final PlanDiscountRuleRepository planDiscountRuleRepository;
    private final List<ValidationBooking> validations;

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO dto) throws MyException {

        // 1. Autenticación y validación de rol
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER) {
            throw new MyException("Solo los dueños pueden seleccionar su plan");
        }

        // Recorre las validaciones antes de hacer toda la carga de datos
        for (ValidationBooking v : validations) {
            v.validate(dto);
        }

        // 2. Cargar owner y mascota
        User owner = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        Pet pet = petRepository.findById(dto.petId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // 3. Crear booking desde mapper
        Booking booking = bookingCreateMapper.toEntity(dto);
        booking.setOwner(owner);
        booking.setPet(pet);

        // 4. Asignar offering, combo y plan con validación explícita
        if (dto.offeringId() != null) {
            booking.setOffering(offeringRepository.findById(dto.offeringId())
                    .orElseThrow(() -> new MyException("El servicio seleccionado no existe")));
        }

        if (dto.comboOfferingId() != null) {
            booking.setComboOffering(comboRepository.findById(dto.comboOfferingId())
                    .orElseThrow(() -> new MyException("El combo seleccionado no existe")));
        }

        Plan userPlan = planRepository.findByOwnerId(user.getId()).orElse(null);
        booking.setPlan(userPlan);

        // 5. Asignar horarios y marcar como PENDIENTES
        List<Schedule> schedules = scheduleRepository.findAllById(dto.scheduleIds());

        List<Long> noDisponibles = schedules.stream()
                .filter(s -> s.getStatus() != ScheduleStatus.DISPONIBLE)
                .map(Schedule::getScheduleId)
                .toList();

        if (!noDisponibles.isEmpty()) {
            throw new MyException("Los siguientes horarios no están disponibles: " + noDisponibles);
        }

        schedules.forEach(s -> s.setStatus(ScheduleStatus.PENDIENTE));
        scheduleRepository.saveAll(schedules);

        booking.setSchedules(schedules);
        booking.setReservationDate(Instant.now());
        booking.setStatus(BookingStatusEnum.PENDIENTE);

        // 6. Asignar profesionales (conversión de ids a entidades)
        List<User> professionals = dto.professionals().stream()
                .map(id -> professionalRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Professional not found: " + id)))
                .toList();

        booking.setProfessionals(professionals);

        // 7. Calcular precio final con descuento del plan
        booking.setFinalPrice(calculateBookingPrice(booking));

        // 8. Guardar booking
        bookingRepository.save(booking);

        return mapper.toDTO(booking);
    }

    public BigDecimal calculateBookingPrice(Booking booking) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal comboTotal = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal promotionAmount = BigDecimal.ZERO;

        // Servicio individual
        if (booking.getOffering() != null && booking.getOffering().getBasePrice() != null) {
            totalPrice = totalPrice.add(booking.getOffering().getBasePrice());
        }

        // Combo
        if (booking.getComboOffering() != null && booking.getComboOffering().getOfferings() != null) {
            comboTotal = booking.getComboOffering().getOfferings().stream()
                    .map(Offering::getBasePrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal comboDiscountRate = BigDecimal.valueOf(booking.getComboOffering().getDiscount()).divide(BigDecimal.valueOf(100));
            discountAmount = comboTotal.multiply(comboDiscountRate);
            totalPrice = totalPrice.add(comboTotal.subtract(discountAmount));
        }

        // Plan
        if (booking.getPlan() != null && booking.getPlan().getPromotion() != null && totalPrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal promotionRate = BigDecimal.valueOf(booking.getPlan().getPromotion()).divide(BigDecimal.valueOf(100));
            promotionAmount = totalPrice.multiply(promotionRate);
            totalPrice = totalPrice.subtract(promotionAmount);
        }
        return totalPrice.max(BigDecimal.ZERO);
    }

    //Simulación para el Profesional para saber si es viable el descuento que aplica
    public BookingSimulationResponseDTO simulateBooking(BookingSimulationRequestDTO dto) {
        BigDecimal servicePrice = BigDecimal.ZERO;
        BigDecimal comboPrice = BigDecimal.ZERO;
        BigDecimal comboDiscountAmount = BigDecimal.ZERO;
        BigDecimal ruleDiscountAmount = BigDecimal.ZERO;

        // 1. Precio del servicio individual
        if (dto.offeringId() != null) {
            servicePrice = offeringRepository.findById(dto.offeringId())
                    .map(Offering::getBasePrice)
                    .orElse(BigDecimal.ZERO);
        }

        // 2. Precio del combo y su descuento
        if (dto.comboOfferingId() != null) {
            ComboOffering combo = comboRepository.findById(dto.comboOfferingId()).orElse(null);
            if (combo != null && combo.getOfferings() != null) {
                comboPrice = combo.getOfferings().stream()
                        .map(Offering::getBasePrice)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal comboDiscountRate = BigDecimal.valueOf(combo.getDiscount()).divide(BigDecimal.valueOf(100));
                comboDiscountAmount = comboPrice.multiply(comboDiscountRate);
            }
        }

        // 3. Total bruto antes del plan
        BigDecimal totalBeforeRule = servicePrice.add(comboPrice).subtract(comboDiscountAmount);

        // 4. Descuento por regla de negocio
        if (dto.category() != null) {
            PlanDiscountRule rule = planDiscountRuleRepository.findByCategory(dto.category()).orElse(null);
            if (rule != null && rule.getDiscount() != null) {
                BigDecimal ruleDiscountRate = rule.getDiscount().divide(BigDecimal.valueOf(100));
                ruleDiscountAmount = totalBeforeRule.multiply(ruleDiscountRate);
            }
        }

        // 5. Precio final
        BigDecimal finalPrice = totalBeforeRule.subtract(ruleDiscountAmount).max(BigDecimal.ZERO);

        // 6. Evaluación contable
        boolean isViable = finalPrice.compareTo(BigDecimal.valueOf(1000)) >= 0;
        String recommendation = isViable
                ? "El descuento es viable para el negocio."
                : "El precio final es demasiado bajo. Considerá ajustar la regla o el combo.";

        return new BookingSimulationResponseDTO(
                servicePrice.add(comboPrice), // basePrice bruto
                comboDiscountAmount,
                ruleDiscountAmount,
                finalPrice,
                isViable,
                recommendation
        );
    }
}

