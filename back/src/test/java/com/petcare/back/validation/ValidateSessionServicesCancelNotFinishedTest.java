package com.petcare.back.validation;

import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ServiceSessionStatus;
import com.petcare.back.exception.MyException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ValidateSessionServicesCancelNotFinishedTest {

    private final ValidateSessionServicesCancelNotFinished validator = new ValidateSessionServicesCancelNotFinished();

    @Test
    void shouldThrowIfSessionIsFinished() {
        ServiceSession session = new ServiceSession();
        session.setStatus(ServiceSessionStatus.FINALIZADO);

        assertThatThrownBy(() -> validator.validate(new User(), session))
                .isInstanceOf(MyException.class)
                .hasMessageContaining("ya finalizada");
    }

    @Test
    void shouldPassIfSessionIsNotFinished() {
        ServiceSession session = new ServiceSession();
        session.setStatus(ServiceSessionStatus.EN_PROGRESO);

        assertThatCode(() -> validator.validate(new User(), session)).doesNotThrowAnyException();
    }
}

