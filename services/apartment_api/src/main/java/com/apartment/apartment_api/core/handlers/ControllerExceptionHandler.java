package com.apartment.apartment_api.core.handlers;

import com.apartment.apartment_api.core.exceptions.SpecificationException;
import com.apartment.apartment_api.core.models.dtos.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> dataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex, WebRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SpecificationException.class)
    public ResponseEntity<ErrorMessage> usernameNotFoundException(NoSuchElementException ex, WebRequest request) {
        return getResponseEntity(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> resourceException(Exception ex, WebRequest request) {
        return getResponseEntity(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorMessage> getResponseEntity(Exception ex, WebRequest request, HttpStatus status) {
        log.error(ex.getMessage(), ex);
        ErrorMessage message = new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<ErrorMessage>(message, status);
    }
}