package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.enumerated.ComboEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ComboOfferingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateComboOfferingUniquenessTest {

    @InjectMocks
    private ValidateComboOfferingUniqueness validator;

    @Mock
    private ComboOfferingRepository comboOfferingRepository;

    @Test
    void shouldThrowIfNameAlreadyExists() {
        ComboOfferingCreateDTO dto = mockDto("PASEO_ASEO", List.of(1L, 2L));
        when(comboOfferingRepository.existsByName(dto.name())).thenReturn(true);

        assertThrows(MyException.class, () -> validator.validate(dto, List.of()));
    }

    @Test
    void shouldThrowIfDuplicateServices() {
        ComboOfferingCreateDTO dto = mockDto("PASEO_ASEO", List.of(1L, 1L));
        when(comboOfferingRepository.existsByName(dto.name())).thenReturn(false);

        assertThrows(MyException.class, () -> validator.validate(dto, List.of()));
    }

    @Test
    void shouldPassIfNameIsUniqueAndServicesAreDistinct() {
        ComboOfferingCreateDTO dto = mockDto("PASEO_ASEO", List.of(1L, 2L));
        when(comboOfferingRepository.existsByName(dto.name())).thenReturn(false);

        assertDoesNotThrow(() -> validator.validate(dto, List.of()));
    }

    private ComboOfferingCreateDTO mockDto(String name, List<Long> ids) {
        return new ComboOfferingCreateDTO(ComboEnum.valueOf(name), "desc", 10.00, ids);
    }
}
