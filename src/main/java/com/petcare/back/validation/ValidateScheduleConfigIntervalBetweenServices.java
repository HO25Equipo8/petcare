package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateScheduleConfigIntervalBetweenServices implements ValidationScheduleConfig{

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        if (dto.intervalBetweenServices() < 0) {
            throw new MyException("El intervalo entre servicios no puede ser negativo.");
        }
        if (dto.intervalBetweenServices() > 180) {
            throw new MyException("El intervalo entre servicios no puede superar las 3 horas.");
        }
    }
}
