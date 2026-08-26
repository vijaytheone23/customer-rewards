package com.charter.reward.exception;

/**
 * Exception thrown when a client provides an invalid rewards request.
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}