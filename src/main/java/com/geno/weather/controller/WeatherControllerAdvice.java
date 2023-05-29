package com.geno.weather.controller;

import com.geno.weather.error.ErrorResponse;
import com.geno.weather.error.InvalidCoordinatesException;
import com.geno.weather.error.InvalidIPAddressException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WeatherControllerAdvice {

    @ExceptionHandler(InvalidIPAddressException.class)
    public ResponseEntity<ErrorResponse> handleInvalidIPAddressException(RuntimeException ex, WebRequest request) {
        //ex.printStackTrace();
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .code(400)
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(InvalidCoordinatesException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCoordinatesException(RuntimeException ex, WebRequest request) {
        //ex.printStackTrace();
        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .code(400)
                        .message(ex.getMessage())
                        .build()
        );
    }
}
