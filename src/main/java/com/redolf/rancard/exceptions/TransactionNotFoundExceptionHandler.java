package com.redolf.rancard.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class TransactionNotFoundExceptionHandler {
    @ExceptionHandler(value = {TransactionNotFoundException.class})
    public ResponseEntity<Object> handleException(TransactionNotFoundException exception){
        final ExceptionPayLoad payLoad =  ExceptionPayLoad.builder()
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
        return new ResponseEntity<>(payLoad,HttpStatus.NOT_FOUND);
    }
}
