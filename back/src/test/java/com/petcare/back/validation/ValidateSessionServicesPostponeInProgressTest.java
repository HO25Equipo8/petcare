package com.petcare.back.validation;

import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ServiceSessionStatus;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ValidateSessionServicesPostponeInProgressTest {

    private final ValidateSessionServicesPostponeInProgress validator = new ValidateSessionServicesPostponeInProgress();

    @Test
    void shouldThrowIfSessionIsNotInProgress() {
        ServiceSession session = new ServiceSession();
        session.setStatus(ServiceSessionStatus.CANCELADO);

        assertThatThrownBy(() -> validator.validate(new User(), session))
                .isInstanceOf(MyException.class)
                .hasMessageContaining("Solo se puede postergar");
    }

    @Test
    void shouldPassIfSessionIsInProgress() {
        ServiceSession session = new ServiceSession();
        session.setStatus(ServiceSessionStatus.EN_PROGRESO);

        assertThatCode(() -> validator.validate(new User(), session)).doesNotThrowAnyException();
    }
}
