package com.charter.reward.exception;

import java.time.LocalDateTime;

/**
 * Represents a standardized error response returned by the REST API.
 *
 * <p>Contains the timestamp, HTTP status, error type and descriptive
 * error message.</p>
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message) {
}