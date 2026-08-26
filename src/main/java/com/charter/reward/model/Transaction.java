package com.charter.reward.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a purchase transaction made by a customer.
 *
 * <p>Each transaction contains the customer ID, transaction date,
 * transaction ID and purchase amount.</p>
 */

public record Transaction(
        String transactionId,
        String customerId,
        LocalDate transactionDate,
        BigDecimal amount
) {
}
