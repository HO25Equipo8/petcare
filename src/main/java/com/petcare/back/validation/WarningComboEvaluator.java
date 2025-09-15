package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;

import java.util.List;

public interface WarningComboEvaluator {
    List<String> evaluar(ComboOfferingCreateDTO dto, List<Offering> offerings);
}
