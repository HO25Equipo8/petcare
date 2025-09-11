package com.petcare.back.validation;

import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ServiceSessionStatus;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public class ValidateSessionServicesCancelNotFinished implements ValidationSessionServices{
    @Override
    public void validate(User user, ServiceSession session) throws MyException {

        if (session.getStatus() == ServiceSessionStatus.FINALIZADO) {
            throw new MyException("No se puede cancelar una sesión ya finalizada");
        }
    }
}
