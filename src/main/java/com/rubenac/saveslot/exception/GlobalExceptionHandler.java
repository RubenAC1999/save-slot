package com.rubenac.saveslot.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError createApiError(Exception exception, int status, HttpServletRequest request) {
        return new ApiError(exception.getMessage(), status, request.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String message = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation error: {}", message);
        ApiError error = createApiError(exception, HttpStatus.BAD_REQUEST.value(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException exception, HttpServletRequest request) {
        log.warn("User not found");
        ApiError error = createApiError(exception, HttpStatus.NOT_FOUND.value(), request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException exception,
                                                            HttpServletRequest request) {
        log.warn("User already exists");
        ApiError error = createApiError(exception, HttpStatus.CONFLICT.value(), request);


        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ApiError> handleGameNotFound(GameNotFoundException exception, HttpServletRequest request) {
        log.warn("Game not found");
        ApiError error = createApiError(exception, HttpStatus.NOT_FOUND.value(), request);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserGameNotFoundException.class)
    public ResponseEntity<ApiError> handleUserGameNotFound(UserGameNotFoundException exception,
                                                           HttpServletRequest request) {
        log.warn("UserGame not found");
        ApiError error = createApiError(exception, HttpStatus.NOT_FOUND.value(), request);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
