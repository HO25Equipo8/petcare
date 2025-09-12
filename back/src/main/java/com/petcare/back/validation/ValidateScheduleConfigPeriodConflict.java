package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ScheduleConfigRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateScheduleConfigPeriodConflict implements ValidationScheduleConfig{

    private final ScheduleConfigRepository configRepository;

    public ValidateScheduleConfigPeriodConflict(ScheduleConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void validate(ScheduleConfigCreateDTO dto) throws MyException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        // Validar conflictos reales por día y horario
        for (ScheduleTurnCreateDTO turn : dto.turns()) {
            boolean conflict = configRepository.existsConflictingTurn(
                    user,
                    dto.startDate(),
                    dto.endDate(),
                    List.of(turn.day()),
                    turn.startTime(),
                    turn.endTime()
            );

            if (conflict) {
                throw new MyException("Ya tienes una configuración que se solapa en día y horario.");
            }
        }

        // Si no hay turnos definidos, validar por fechas
        if (dto.turns().isEmpty()) {
            boolean overlaps = configRepository.existsOverlappingConfig(
                    user,
                    dto.startDate(),
                    dto.endDate()
            );

            if (overlaps) {
                throw new MyException("Ya tienes una configuración que cubre parte de ese período.");
            }
        }
    }
}

