package com.empik.empikinterviewtask.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public RestApiError handleEntityNotFoundException(EntityNotFoundException ex) {
        return new RestApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
    }

}
