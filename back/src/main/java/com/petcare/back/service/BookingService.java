package com.petcare.back.service;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingSimulationRequestDTO;
import com.petcare.back.domain.dto.response.BookingListDTO;
import com.petcare.back.domain.dto.response.BookingResponseDTO;
import com.petcare.back.domain.dto.response.BookingSimulationResponseDTO;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.domain.mapper.request.BookingCreateMapper;
import com.petcare.back.domain.mapper.response.BookingResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.*;
import com.petcare.back.validation.ValidationBooking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
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
    private final PlanDiscountRuleRepository planDiscountRuleRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository professionalRepository;
    private final BookingResponseMapper mapper;
    private final BookingCreateMapper bookingCreateMapper;
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

        // Descuento por categoría del cliente según reglas del SITTER
        if (booking.getOwner() != null && booking.getProfessionals() != null && !booking.getProfessionals().isEmpty()) {
            User sitter = booking.getProfessionals().get(0);
            CustomerCategory category = calculateCustomerCategory(booking.getOwner(), sitter);

            Optional<PlanDiscountRule> ruleOpt = planDiscountRuleRepository.findByCategoryAndSitter(category, sitter);
            if (ruleOpt.isPresent()) {
                BigDecimal rate = ruleOpt.get().getDiscount().divide(BigDecimal.valueOf(100));
                BigDecimal categoryDiscount = totalPrice.multiply(rate);
                totalPrice = totalPrice.subtract(categoryDiscount);
            }
        }

        return totalPrice.max(BigDecimal.ZERO);
    }

    public CustomerCategory calculateCustomerCategory(User owner, User sitter) {

        int totalBookings = bookingRepository.countByOwnerAndSitter(owner.getId(), sitter.getId());
        List<PlanDiscountRule> rules = planDiscountRuleRepository.findBySitterId(sitter.getId());

        for (PlanDiscountRule rule : rules) {
            double min = rule.getMinSessionsPerWeek();
            double max = rule.getMaxSessionsPerWeek();

            if (totalBookings >= min && totalBookings <= max) {
                return rule.getCategory();
            }
        }

        return CustomerCategory.NORMAL; // o BASE si querés agregarlo como default
    }

    @Transactional
    public BookingResponseDTO updateBookingStatus(Long bookingId, BookingStatusEnum newStatus, User actor) throws MyException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MyException("Reserva no encontrada"));

        boolean isOwner = booking.getOwner().equals(actor);
        boolean isProfessional = booking.getProfessionals().contains(actor);

        if (!isOwner && !isProfessional) {
            throw new MyException("No tienes permisos para modificar esta reserva");
        }

        if (booking.getStatus() == BookingStatusEnum.CANCELADO || booking.getStatus() == BookingStatusEnum.COMPLETADO) {
            throw new MyException("No se puede modificar una reserva cancelada o completada");
        }

        booking.setStatus(newStatus);

        // Si se cancela, liberar horarios
        if (newStatus == BookingStatusEnum.CANCELADO) {
            booking.getSchedules().forEach(s -> s.setStatus(ScheduleStatus.DISPONIBLE));
            scheduleRepository.saveAll(booking.getSchedules());
        }

        bookingRepository.save(booking);
        return mapper.toDTO(booking);
    }

    public BookingResponseDTO confirmBooking(Long bookingId, User sitter) throws MyException {
        return updateBookingStatus(bookingId, BookingStatusEnum.CONFIRMADO, sitter);
    }

    public BookingResponseDTO cancelBooking(Long bookingId, User sitter) throws MyException {
        return updateBookingStatus(bookingId, BookingStatusEnum.CANCELADO, sitter);
    }

    //Metodo para que el sitter pueda reprogramar una reserva
    @Transactional
    public BookingResponseDTO rescheduleBooking(Long bookingId, List<Long> newScheduleIds, User sitter) throws MyException {
        //Validar y actualizar estado a REPROGRAMAR
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MyException("Reserva no encontrada"));

        if (!booking.getProfessionals().contains(sitter)) {
            throw new MyException("No tienes permisos para reprogramar esta reserva");
        }

        if (booking.getStatus() != BookingStatusEnum.CONFIRMADO && booking.getStatus() != BookingStatusEnum.PENDIENTE) {
            throw new MyException("Solo se pueden reprogramar reservas activas");
        }

        booking.setStatus(BookingStatusEnum.REPROGRAMAR);
        bookingRepository.save(booking);

        //Aplicar nuevos horarios y pasar a PENDIENTE_REPROGRAMAR
        List<Schedule> nuevosHorarios = scheduleRepository.findAllById(newScheduleIds);
        nuevosHorarios.forEach(s -> s.setStatus(ScheduleStatus.PENDIENTE));
        scheduleRepository.saveAll(nuevosHorarios);

        booking.getSchedules().forEach(s -> s.setStatus(ScheduleStatus.DISPONIBLE));
        booking.setSchedules(nuevosHorarios);

        booking.setStatus(BookingStatusEnum.PENDIENTE_REPROGRAMAR);
        bookingRepository.save(booking);

        return mapper.toDTO(booking);
    }

    //Metodo para que el dueño acepte la reprogramacion de la reserva
    @Transactional
    public BookingResponseDTO respondToReschedule(Long bookingId, boolean accept, User owner) throws MyException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MyException("Reserva no encontrada"));

        if (!booking.getOwner().equals(owner)) {
            throw new MyException("Solo el dueño puede responder esta reprogramación");
        }

        if (booking.getStatus() != BookingStatusEnum.PENDIENTE_REPROGRAMAR) {
            throw new MyException("La reserva no está esperando reprogramación");
        }

        if (accept) {
            booking.setStatus(BookingStatusEnum.CONFIRMADO);
        } else {
            booking.getSchedules().forEach(s -> s.setStatus(ScheduleStatus.DISPONIBLE));
            scheduleRepository.saveAll(booking.getSchedules());
            booking.setStatus(BookingStatusEnum.CANCELADO); // o volver a PENDIENTE si querés permitir otro intento
        }

        bookingRepository.save(booking);
        return mapper.toDTO(booking);
    }

    public List<BookingListDTO> getBookingsForUser(User user) {
        List<Booking> bookings;

        if (user.getRole() == Role.OWNER) {
            bookings = bookingRepository.findByOwnerId(user.getId());
        } else if (user.getRole() == Role.SITTER || user.getRole() == Role.ADMIN) {
            bookings = bookingRepository.findByProfessionalsContaining(user);
        } else {
            bookings = List.of(); // o lanzar excepción si querés limitar
        }

        return bookings.stream()
                .map(this::toSummaryDTO)
                .toList();
    }

    private BookingListDTO toSummaryDTO(Booking booking) {
        return new BookingListDTO(
                booking.getId(),
                booking.getPet().getName(),
                booking.getOffering() != null ? booking.getOffering().getName().getLabel() : "Combo",
                booking.getReservationDate(),
                booking.getStatus(),
                booking.getSchedules().stream()
                        .map(Schedule::getEstablishedTime)
                        .min(Comparator.naturalOrder())
                        .orElse(null)
        );
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

