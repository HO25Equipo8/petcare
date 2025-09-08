package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.UserUpdateDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;

public interface ValidationUserProfile{

    void validate(UserUpdateDTO dto,  User authenticatedUser) throws MyException;
}
