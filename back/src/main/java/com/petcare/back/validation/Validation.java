package com.petcare.back.validation;

import com.petcare.back.exception.MyException;

public interface Validation<T> {
    void validate(T data) throws MyException;
}
