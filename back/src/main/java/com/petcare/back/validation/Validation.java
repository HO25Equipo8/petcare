package com.petcare.back.validation;

import com.petcare.back.exception.MyException;

public interface Validation<T> {
    void validar(T data) throws MyException;
}
