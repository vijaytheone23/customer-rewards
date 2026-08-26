package com.charter.reward.exception;

/**
 * Exception thrown when the requested customer does not exist.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String customerId) {
        super("Customer not found: " + customerId);
    }
}