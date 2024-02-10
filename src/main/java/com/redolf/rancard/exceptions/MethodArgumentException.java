package com.redolf.rancard.exceptions;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class MethodArgumentException extends MethodArgumentNotValidException {
    public MethodArgumentException(MethodParameter parameter, BindingResult bindingResult) {
        super(parameter, bindingResult);
    }
}
