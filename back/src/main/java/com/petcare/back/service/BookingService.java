package com.petcare.back.service;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingDataByEmailDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.domain.dto.request.BookingSimulationRequestDTO;
import com.petcare.back.domain.dto.response.BookingListDTO;
import com.petcare.back.domain.dto.response.BookingResponseDTO;
import com.petcare.back.domain.dto.response.BookingSimulationResponseDTO;
import com.petcare.back.domain.dto.response.ServiceItemResponseDTO;
import com.petcare.back.domain.entity.*;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.enumerated.CustomerCategory;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.domain.mapper.request.BookingCreateMapper;
import com.petcare.back.domain.mapper.response.BookingResponseMapper;
import com.petcare.back.domain.mapper.response.ServiceItemMapper;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
    private final List<ValidationBooking> validations;
    private final EmailService emailService;
    private final BookingServiceItemRepository bookingServiceItemRepository;
    private final ServiceItemMapper serviceItemMapper;

    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO dto) throws MyException {

        // 1. Autenticación y validación de rol
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER) {
            throw new MyException("Solo los dueños pueden seleccionar su plan");
        }

        // 2. Validaciones externas
        for (ValidationBooking v : validations) {
            v.validate(dto);
        }

        // 3. Cargar owner y mascota
        User owner = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        Pet pet = petRepository.findById(dto.petId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // 4. Crear booking base
        Booking booking = new Booking();
        booking.setOwner(owner);
        booking.setPet(pet);
        booking.setReservationDate(Instant.now());
        booking.setStatus(BookingStatusEnum.PENDIENTE);

        // 5. Asignar combo si corresponde
        if (dto.comboOfferingId() != null) {
            ComboOffering combo = comboRepository.findById(dto.comboOfferingId())
                    .orElseThrow(() -> new MyException("El combo seleccionado no existe"));
            booking.setComboOffering(combo);
        }

        // 6. Crear ítems del combo
        List<BookingServiceItem> serviceItems = new ArrayList<>();

        for (BookingServiceItemCreateDTO itemDTO : dto.items()) {
            Offering offering = offeringRepository.findById(itemDTO.offeringId())
                    .orElseThrow(() -> new MyException("Servicio no encontrado"));

            Schedule schedule = scheduleRepository.findById(itemDTO.scheduleId())
                    .orElseThrow(() -> new MyException("Horario no encontrado"));

            if (schedule.getStatus() != ScheduleStatus.DISPONIBLE) {
                throw new MyException("Horario no disponible: " + schedule.getScheduleId());
            }

            schedule.setStatus(ScheduleStatus.PENDIENTE);
            scheduleRepository.save(schedule);

            User professional = professionalRepository.findById(itemDTO.professionalId())
                    .orElseThrow(() -> new MyException("Profesional no encontrado"));

            BookingServiceItem item = new BookingServiceItem();
            item.setBooking(booking);
            item.setOffering(offering);
            item.setSchedule(schedule);
            item.setProfessional(professional);
            item.setStatus(BookingStatusEnum.PENDIENTE);
            item.setPrice(offering.getBasePrice());

            serviceItems.add(item);
        }

        booking.setServiceItems(serviceItems);

        // 7. Calcular precio final
        booking.setFinalPrice(calculateBookingPrice(booking));

        // 8. Guardar booking
        bookingRepository.save(booking);

        return mapper.toDTO(booking);
    }

    public BigDecimal calculateBookingPrice(Booking booking) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal comboTotal = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;

        // 1. Sumar precios base desde los ítems
        totalPrice = booking.getServiceItems().stream()
                .map(BookingServiceItem::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Aplicar descuento del combo si corresponde
        if (booking.getComboOffering() != null && booking.getComboOffering().getDiscount() != null) {
            comboTotal = booking.getComboOffering().getOfferings().stream()
                    .map(Offering::getBasePrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal comboDiscountRate = BigDecimal.valueOf(booking.getComboOffering().getDiscount())
                    .divide(BigDecimal.valueOf(100));
            discountAmount = comboTotal.multiply(comboDiscountRate);
            totalPrice = totalPrice.subtract(discountAmount);
        }

        // 3. Aplicar descuento por categoría del cliente
        if (booking.getOwner() != null) {
            // Tomamos el primer profesional de los ítems (si hay)
            Optional<User> sitterOpt = booking.getServiceItems().stream()
                    .map(BookingServiceItem::getProfessional)
                    .filter(Objects::nonNull)
                    .findFirst();

            if (sitterOpt.isPresent()) {
                User sitter = sitterOpt.get();
                CustomerCategory category = calculateCustomerCategory(booking.getOwner(), sitter);

                Optional<PlanDiscountRule> ruleOpt = planDiscountRuleRepository.findByCategoryAndSitter(category, sitter);
                if (ruleOpt.isPresent()) {
                    BigDecimal rate = ruleOpt.get().getDiscount().divide(BigDecimal.valueOf(100));
                    BigDecimal categoryDiscount = totalPrice.multiply(rate);
                    totalPrice = totalPrice.subtract(categoryDiscount);
                }
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
        return CustomerCategory.NORMAL;
    }

    @Transactional
    public BookingResponseDTO updateBookingStatus(Long bookingId, BookingStatusEnum newStatus, User actor) throws MyException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MyException("Reserva no encontrada"));

        boolean isOwner = booking.getOwner().equals(actor);

        boolean isProfessional = booking.getServiceItems().stream()
                .map(BookingServiceItem::getProfessional)
                .anyMatch(prof -> prof.equals(actor));

        if (!isOwner && !isProfessional) {
            throw new MyException("No tienes permisos para modificar esta reserva");
        }

        if (booking.getStatus() == BookingStatusEnum.CANCELADO || booking.getStatus() == BookingStatusEnum.COMPLETADO) {
            throw new MyException("No se puede modificar una reserva cancelada o completada");
        }

        booking.setStatus(newStatus);

        // 🔄 Sincronizar estado de horarios e ítems
        ScheduleStatus scheduleStatus = switch (newStatus) {
            case CANCELADO -> ScheduleStatus.CANCELADO;
            case CONFIRMADO -> ScheduleStatus.RESERVADO;
            case PENDIENTE, PENDIENTE_REPROGRAMAR -> ScheduleStatus.PENDIENTE;
            case COMPLETADO -> ScheduleStatus.EXPIRADO;
            case REPROGRAMAR -> ScheduleStatus.DISPONIBLE;
        };

        for (BookingServiceItem item : booking.getServiceItems()) {
            item.setStatus(newStatus); // reflejar estado del booking en el ítem
            item.getSchedule().setStatus(scheduleStatus);
            scheduleRepository.save(item.getSchedule());
        }

        bookingRepository.save(booking);
        return mapper.toDTO(booking);
    }

    @Transactional
    public BookingResponseDTO updateServiceItemStatus(Long itemId, BookingStatusEnum newStatus, User actor) throws MyException {
        BookingServiceItem item = bookingServiceItemRepository.findById(itemId)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        Booking booking = item.getBooking();

        boolean isOwner = booking.getOwner().equals(actor);
        boolean isProfessional = item.getProfessional().equals(actor);

        if (!isOwner && !isProfessional) {
            throw new MyException("No tienes permisos para modificar este servicio");
        }

        if (item.getStatus() == BookingStatusEnum.CANCELADO || item.getStatus() == BookingStatusEnum.COMPLETADO) {
            throw new MyException("No se puede modificar un servicio cancelado o completado");
        }

        item.setStatus(newStatus);

        ScheduleStatus scheduleStatus = switch (newStatus) {
            case CANCELADO -> ScheduleStatus.CANCELADO;
            case CONFIRMADO -> ScheduleStatus.RESERVADO;
            case PENDIENTE, PENDIENTE_REPROGRAMAR -> ScheduleStatus.PENDIENTE;
            case COMPLETADO -> ScheduleStatus.EXPIRADO;
            case REPROGRAMAR -> ScheduleStatus.DISPONIBLE;
        };

        item.getSchedule().setStatus(scheduleStatus);
        scheduleRepository.save(item.getSchedule());
        bookingServiceItemRepository.save(item);

        return mapper.toDTO(booking);
    }

    public BookingResponseDTO confirmBooking(Long bookingId, User sitter) throws MyException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MyException("Reserva no encontrada"));

        List<BookingServiceItem> itemsDelSitter = booking.getServiceItems().stream()
                .filter(item -> item.getProfessional().equals(sitter))
                .toList();

        for (BookingServiceItem item : itemsDelSitter) {
            confirmItem(item.getId(), sitter); // ya envía email por ítem
        }

        return mapper.toDTO(booking);
    }
    @Transactional
    public void confirmItem(Long itemId, User sitter) throws MyException {
        BookingServiceItem item = bookingServiceItemRepository.findById(itemId)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        if (!item.getProfessional().equals(sitter)) {
            throw new MyException("No tienes permisos para confirmar este servicio");
        }

        if (item.getStatus() == BookingStatusEnum.CANCELADO || item.getStatus() == BookingStatusEnum.COMPLETADO) {
            throw new MyException("Este servicio ya no puede confirmarse");
        }

        item.setStatus(BookingStatusEnum.CONFIRMADO);
        item.getSchedule().setStatus(ScheduleStatus.RESERVADO);

        scheduleRepository.save(item.getSchedule());
        bookingServiceItemRepository.save(item);

//        try {
//            BookingDataByEmailDTO emailData = buildServiceItemEmailData(item);
//            emailService.sendBookingConfirmationEmail(emailData);
//        } catch (Exception e) {
//            // logueamos el error, pero no rompemos la transacción
//            System.err.println("[confirmItem] ERROR enviando email: " + e.getMessage());
//            e.printStackTrace();
//        }
    }


    public BookingResponseDTO cancelBooking(Long bookingId, User sitter, String reason) throws MyException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new MyException("Reserva no encontrada"));

        List<BookingServiceItem> itemsDelSitter = booking.getServiceItems().stream()
                .filter(item -> item.getProfessional().equals(sitter))
                .toList();

        for (BookingServiceItem item : itemsDelSitter) {
            cancelItem(item.getId(), sitter, reason);
        }

        return mapper.toDTO(booking);
    }

    @Transactional
    public void cancelItem(Long itemId, User sitter, String reason) throws MyException {
        BookingServiceItem item = bookingServiceItemRepository.findById(itemId)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        if (!item.getProfessional().equals(sitter)) {
            throw new MyException("No tienes permisos para cancelar este servicio");
        }

        if (item.getStatus() == BookingStatusEnum.CANCELADO || item.getStatus() == BookingStatusEnum.COMPLETADO) {
            throw new MyException("Este servicio ya no puede cancelarse");
        }

        item.setStatus(BookingStatusEnum.CANCELADO);
        item.getSchedule().setStatus(ScheduleStatus.DISPONIBLE);

        System.out.println("Cancelando itemId=" + itemId + ", sitter=" + sitter.getId() +
                ", item.professional=" + item.getProfessional().getId());

        scheduleRepository.save(item.getSchedule());
        bookingServiceItemRepository.save(item);
        bookingServiceItemRepository.flush();

//        try {
//            BookingDataByEmailDTO emailData = buildServiceItemEmailData(item);
//            emailService.sendBookingCancellationEmail(emailData, reason);
//        } catch (Exception e) {
//            // logueamos el error, pero no rompemos la transacción
//            System.err.println("[confirmItem] ERROR enviando email: " + e.getMessage());
//            e.printStackTrace();
//        }
    }


    //Metodo para que el sitter pueda reprogramar una reserva
    @Transactional
    public void rescheduleItem(Long itemId, Long newScheduleId, User sitter) throws MyException {
        BookingServiceItem item = bookingServiceItemRepository.findById(itemId)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        if (!item.getProfessional().equals(sitter)) {
            throw new MyException("No tienes permisos para reprogramar este servicio");
        }

        if (item.getStatus() != BookingStatusEnum.CONFIRMADO && item.getStatus() != BookingStatusEnum.PENDIENTE) {
            throw new MyException("Solo se pueden reprogramar servicios activos");
        }

        Schedule nuevoHorario = scheduleRepository.findById(newScheduleId)
                .orElseThrow(() -> new MyException("Horario no encontrado"));

        if (nuevoHorario.getStatus() != ScheduleStatus.DISPONIBLE) {
            throw new MyException("El nuevo horario no está disponible");
        }

        // Cancelar el horario anterior
        item.getSchedule().setStatus(ScheduleStatus.CANCELADO);
        scheduleRepository.save(item.getSchedule());

        // Asignar nuevo horario
        nuevoHorario.setStatus(ScheduleStatus.PENDIENTE);
        scheduleRepository.save(nuevoHorario);

        item.setSchedule(nuevoHorario);
        item.setStatus(BookingStatusEnum.PENDIENTE_REPROGRAMAR);
        bookingServiceItemRepository.save(item);

//        try {
//            BookingDataByEmailDTO emailData = buildServiceItemEmailData(item);
//            emailService.sendBookingRescheduleEmail(emailData);
//        } catch (Exception e) {
//            // logueamos el error, pero no rompemos la transacción
//            System.err.println("[confirmItem] ERROR enviando email: " + e.getMessage());
//            e.printStackTrace();
//        }
    }

    //Metodo para que el dueño acepte la reprogramacion de la reserva
    @Transactional
    public void respondToItemReschedule(Long itemId, boolean accept, User owner) throws MyException {
        BookingServiceItem item = bookingServiceItemRepository.findById(itemId)
                .orElseThrow(() -> new MyException("Servicio no encontrado"));

        if (!item.getBooking().getOwner().equals(owner)) {
            throw new MyException("Solo el dueño puede responder esta reprogramación");
        }

        if (item.getStatus() != BookingStatusEnum.PENDIENTE_REPROGRAMAR) {
            throw new MyException("Este servicio no está esperando reprogramación");
        }

        if (accept) {
            item.setStatus(BookingStatusEnum.CONFIRMADO);
            item.getSchedule().setStatus(ScheduleStatus.RESERVADO);
        } else {
            item.setStatus(BookingStatusEnum.CANCELADO);
            item.getSchedule().setStatus(ScheduleStatus.DISPONIBLE);
        }

        scheduleRepository.save(item.getSchedule());
        bookingServiceItemRepository.save(item);
    }

    public List<BookingListDTO> getBookingsForUser(User user) {
        List<Booking> bookings;

        if (user.getRole() == Role.OWNER) {
            bookings = bookingRepository.findByOwnerId(user.getId());
        } else if (user.getRole() == Role.SITTER || user.getRole() == Role.ADMIN) {
            bookings = bookingRepository.findByProfessional(user); // ✅ nueva query
        } else {
            bookings = List.of(); // o lanzar excepción si querés limitar
        }

        return bookings.stream()
                .map(this::toSummaryDTO)
                .toList();
    }


    private BookingListDTO toSummaryDTO(Booking booking) {
        // Obtener el primer ítem (puede ser el más próximo, el primero creado, etc.)
        BookingServiceItem firstItem = booking.getServiceItems().stream()
                .min(Comparator.comparing(item -> item.getSchedule().getEstablishedTime()))
                .orElse(null);

        String serviceLabel = firstItem != null
                ? firstItem.getOffering().getName().getLabel()
                : "Sin servicios";

        Instant nextSession = firstItem != null
                ? firstItem.getSchedule().getEstablishedTime()
                : null;

        Long comboId = booking.getComboOffering() != null
                ? booking.getComboOffering().getId()
                : null;

        List<ServiceItemResponseDTO> items = booking.getServiceItems().stream()
                .map(serviceItemMapper::toDTO)
                .collect(Collectors.toList());

        return new BookingListDTO(
                booking.getId(),
                booking.getPet().getName(),
                serviceLabel,
                booking.getReservationDate(),
                booking.getStatus(),
                nextSession,
                comboId,
                items
        );
    }

    public BookingDataByEmailDTO buildServiceItemEmailData(BookingServiceItem item) {
        Schedule schedule = item.getSchedule();

        Instant establishedTime = schedule.getEstablishedTime();
        LocalDate sessionDate = establishedTime.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime startTime = establishedTime.atZone(ZoneId.systemDefault()).toLocalTime();

        int durationMinutes = schedule.getScheduleConfig().getServiceDurationMinutes();
        LocalTime endTime = startTime.plusMinutes(durationMinutes);

        return new BookingDataByEmailDTO(
                item.getBooking().getOwner().getEmail(),
                item.getBooking().getOwner().getName(),
                item.getProfessional().getName(),
                item.getBooking().getPet().getName(),
                sessionDate.toString(),
                startTime.toString(),
                endTime.toString()
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

