package com.redolf.rancard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class InternalServerErrorHandler {
    @ExceptionHandler(value = {InternalServerErrorException.class})
    public ResponseEntity<Object> handleException(InternalServerErrorException exception) {
        final ExceptionPayLoad payLoad = ExceptionPayLoad.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(payLoad, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
