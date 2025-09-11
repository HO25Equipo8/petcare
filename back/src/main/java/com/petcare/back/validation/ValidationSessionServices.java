package com.petcare.back.validation;

import com.petcare.back.domain.entity.ServiceSession;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;

public interface ValidationSessionServices {
    void validate(User user, ServiceSession session) throws MyException;
}
