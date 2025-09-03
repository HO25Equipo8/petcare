package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.mapper.request.BookingCreateMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.BookingService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ValidateBookingPriceMinimum implements ValidationBooking {

    private final BookingCreateMapper bookingCreateMapper;
    private final BookingService bookingService;

    public ValidateBookingPriceMinimum(BookingCreateMapper bookingCreateMapper,
                                       BookingService bookingService) {
        this.bookingCreateMapper = bookingCreateMapper;
        this.bookingService = bookingService;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        Booking booking = bookingCreateMapper.toEntity(data);
        BigDecimal finalPrice = bookingService.calculateBookingPrice(booking);

        if (finalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MyException("El precio final de la reserva no puede ser cero. Revisá los descuentos aplicados.");
        }
    }
}
