package com.petcare.back.validation;

import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ValidateSessionServicesWithinBookingTime implements ValidationSessionServices{
    private static final int EXTRA_MINUTES = 15;

    @Override
    public void validate(User user, ServiceSession session) {
//        Booking booking = session.getBooking();
//        LocalDateTime now = LocalDateTime.now();
//
//        LocalDateTime[] range = getBookingTimeRange(booking);
//        LocalDateTime start = range[0].minusMinutes(EXTRA_MINUTES);
//        LocalDateTime end = range[1].plusMinutes(EXTRA_MINUTES);
//
//        if (now.isBefore(start) || now.isAfter(end)) {
//            throw new ValidationException("La sesión no puede gestionarse fuera del horario de la reserva.");
//        }
        System.out.println("prueba");
    }

//    private LocalDateTime[] getBookingTimeRange(Booking booking) {
//        List<LocalDateTime> scheduleTimes = booking.getSchedules().stream()
//                .map(s -> LocalDateTime.ofInstant(s.getEstablishedTime(), ZoneId.systemDefault()))
//                .sorted()
//                .toList();
//
//        if (scheduleTimes.isEmpty()) {
//            throw new ValidationException("La reserva no tiene horarios asignados.");
//        }
//
//        int duration = booking.getSchedules().get(0).getScheduleConfig().getServiceDurationMinutes();
//        LocalDateTime start = scheduleTimes.get(0);
//        LocalDateTime end = scheduleTimes.get(scheduleTimes.size() - 1).plusMinutes(duration);
//
//        return new LocalDateTime[]{ start, end };
//    }
}