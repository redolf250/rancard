package com.redolf.rancard.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionPayLoad {
    private HttpStatus httpStatus;
    private ZonedDateTime timeStamp;
    private String message;
}
