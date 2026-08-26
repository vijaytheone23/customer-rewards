package com.charter.reward.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents transaction details included in the reward response.
 */
public record RewardTransaction(
        String transactionId,
        LocalDate transactionDate,
        BigDecimal amount,
        int rewardPoints
) {
}