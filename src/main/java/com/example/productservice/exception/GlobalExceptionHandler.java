package com.example.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ProductNotFoundException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", Instant.now());
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", Instant.now());
        response.put("status", 400);
        response.put("error", "Bad Request");
        response.put("message", "Invalid request");

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}