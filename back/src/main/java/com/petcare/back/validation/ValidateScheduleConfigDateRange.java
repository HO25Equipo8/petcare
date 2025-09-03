package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class ValidateScheduleConfigDateRange implements ValidationScheduleConfig{

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        if (dto.startDate().isAfter(dto.endDate())) {
            throw new MyException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        if (ChronoUnit.DAYS.between(dto.startDate(), dto.endDate()) > 365) {
            throw new MyException("El rango de fechas no puede superar un año.");
        }
    }
}
