package com.petcare.back.validation;

import com.petcare.back.exception.MyException;
import org.springframework.web.multipart.MultipartFile;

public interface ValidationFile {
    void validate(MultipartFile file) throws MyException;
}
