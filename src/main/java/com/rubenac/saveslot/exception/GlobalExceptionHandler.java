package com.rubenac.saveslot.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError createApiError(Exception exception, int status, HttpServletRequest request) {
        return new ApiError(exception.getMessage(), status, request.getRequestURI(), LocalDateTime.now());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleGenericException(Exception exception, HttpServletRequest request) {
//        log.error("Unexpected error: {}", exception.getMessage(), exception);
//        ApiError error = createApiError(exception, HttpStatus.INTERNAL_SERVER_ERROR.value(), request);
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }

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
