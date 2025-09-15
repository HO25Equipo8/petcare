package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.exception.MyException;

import java.util.List;

public interface ValidationCombo{
    void validate(ComboOfferingCreateDTO dto, List<Offering> offerings) throws MyException;
}
