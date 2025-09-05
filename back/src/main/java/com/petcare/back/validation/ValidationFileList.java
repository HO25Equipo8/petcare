package com.petcare.back.validation;

import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import org.springframework.web.multipart.MultipartFile;

public interface ValidationFileList {
    void validate(MultipartFile[] files, User authenticatedUse) throws MyException;
}
