package com.redolf.rancard.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Map;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvalidTransactionExceptionPayLoad {
    private HttpStatus httpStatus;
    private ZonedDateTime timeStamp;
    private Map<String, String> message;
}
