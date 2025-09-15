package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.OfferingVariantDescriptionEnum;
import com.petcare.back.domain.enumerated.OfferingVariantScheduleTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ValidateComboOfferingCompatibility implements WarningComboEvaluator {

    @Override
    public List<String> evaluar(ComboOfferingCreateDTO dto, List<Offering> offerings) {
        long nocturnosNoFlexibles = offerings.stream()
                .filter(o -> getScheduleTypeFromDescription(o.getDescription()) == OfferingVariantScheduleTypeEnum.NOCTURNO)
                .filter(o -> !isFlexible(o.getDescription()))
                .count();

        List<String> warnings = new ArrayList<>();

        if (nocturnosNoFlexibles > 1) {
            warnings.add("Este combo incluye múltiples servicios nocturnos no flexibles. Revisar compatibilidad horaria en la reserva.");
        }

        return warnings;
    }

    private static OfferingVariantScheduleTypeEnum getScheduleTypeFromDescription(String description) {
        return Arrays.stream(OfferingVariantDescriptionEnum.values())
                .filter(v -> v.getDescription().equalsIgnoreCase(description))
                .map(OfferingVariantDescriptionEnum::getScheduleType)
                .findFirst()
                .orElse(null);
    }

    private static boolean isFlexible(String description) {
        return Arrays.stream(OfferingVariantDescriptionEnum.values())
                .filter(v -> v.getDescription().equalsIgnoreCase(description))
                .anyMatch(v -> v.getScheduleType() == OfferingVariantScheduleTypeEnum.FLEXIBLE);
    }
}
