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
import java.time.Instant;
import java.util.List;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER) {
            throw new MyException("Solo los dueños pueden seleccionar su plan");
        }

        User owner = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        Pet pet = petRepository.findById(dto.petId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        Booking booking = bookingCreateMapper.toEntity(dto);

        booking.setOwner(owner);
        booking.setPet(pet);

        if (dto.offeringId() != null)
            booking.setOffering(offeringRepository.findById(dto.offeringId()).orElse(null));

        if (dto.comboOfferingId() != null)
            booking.setComboOffering(comboRepository.findById(dto.comboOfferingId()).orElse(null));

        if (dto.planId() != null)
            booking.setPlan(planRepository.findById(dto.planId()).orElse(null));

        booking.setSchedules(scheduleRepository.findAllById(dto.scheduleIds()));
        booking.setReservationDate(Instant.now());
        booking.setStatus(BookingStatusEnum.PENDIENTE);

        List<BookingProfessional> bookingProfessionals = dto.professionals().stream()
                .map(p -> {
                    User prof = professionalRepository.findById(p.userId())
                            .orElseThrow(() -> new RuntimeException("Professional not found: " + p.userId()));
                    return new BookingProfessional(booking, prof);
                }).toList();
        booking.setProfessionals(bookingProfessionals);

        bookingRepository.save(booking);

        return mapper.toDTO(booking);
    }
}

