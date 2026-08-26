package com.charter.reward.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

/**
 * Provides centralized exception handling for REST API errors.
 *
 * <p>Converts application exceptions into consistent HTTP responses
 * instead of exposing internal exceptions to API clients.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(
            CustomerNotFoundException exception) {

        logger.warn("Customer not found: {}", exception.getMessage());
        return buildError(HttpStatus.NOT_FOUND,
                "Customer Not Found", exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(
            InvalidRequestException exception) {

        logger.warn("Invalid request: {}", exception.getMessage());
        return buildError(HttpStatus.BAD_REQUEST,
                "Invalid Request", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception) {

        logger.warn("Invalid request parameter '{}': {}",
                exception.getName(), exception.getMessage());
        String message = "Invalid value for parameter '" + exception.getName() + "'";
        if (exception.getRequiredType() != null
                && exception.getRequiredType().equals(java.time.LocalDate.class)) {
            message = "Invalid date format for '" + exception.getName()
                    + "'. Please use yyyy-MM-dd";
        }
        return buildError(HttpStatus.BAD_REQUEST, "Invalid Request", message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(
            MissingServletRequestParameterException exception) {

        logger.warn("Missing request parameter: {}", exception.getParameterName());
        return buildError(HttpStatus.BAD_REQUEST, "Invalid Request",
                exception.getParameterName() + " parameter is required");
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleMethodValidationException(
            HandlerMethodValidationException exception) {

        String message = exception.getParameterValidationResults().stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Request validation failed");

        logger.warn("Request validation failed: {}", message);
        return buildError(HttpStatus.BAD_REQUEST, "Invalid Request", message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception) {

        String message = exception.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("Request validation failed");

        logger.warn("Request validation failed: {}", message);
        return buildError(HttpStatus.BAD_REQUEST, "Invalid Request", message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception exception) {

        logger.error("Unexpected error while processing request", exception);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status, String error, String message) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message);

        return ResponseEntity.status(status).body(response);
    }
}
