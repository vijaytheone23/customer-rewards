package com.charter.reward.model;

/**
 * Represents a customer participating in the rewards program.
 *
 * <p>A customer is identified by a unique customer ID and has an associated
 * customer name.</p>
 */


public record Customer(
        String customerId,
        String customerName
) {
}
