package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.IncidentsDTO;
import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.Incidents;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.BookingRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class ValidateIncidentsOutOfService implements ValidationReportIncidents{

    private final BookingRepository bookingRepository;

    public ValidateIncidentsOutOfService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void validate(IncidentsDTO incidentsDTO) throws MyException {
        // Buscar el booking
        Booking booking = bookingRepository.findById(incidentsDTO.getBookingId())
                .orElseThrow(() -> new MyException("La reserva no existe"));

        Instant incidentDate = incidentsDTO.getIncidentsDate();
        if (incidentDate == null) {
            incidentDate = Instant.now();
        }
        final Instant finalIncidentDate = incidentDate;

        boolean isValid = booking.getSchedules().stream().anyMatch(schedule -> {
            Instant start = schedule.getEstablishedTime();
            Instant end = start.plus(45, ChronoUnit.MINUTES);

            return !finalIncidentDate.isBefore(start) && !finalIncidentDate.isAfter(end);
        });

        if (!isValid) {
            throw new MyException("El incidente debe reportarse dentro de la fecha del servicio " +
                    "o  de los 45 minutos del turno asignado");
        }
    }
}