package com.charter.reward.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Calculates reward points according to the customer rewards program rules.
 *
 * <p>Reward calculation:</p>
 *
 * <ul>
 *     <li>Up to $50: 0 points</li>
 *     <li>$50 to $100: 1 point for each dollar above $50</li>
 *     <li>Above $100: 50 points plus 2 points for each dollar above $100</li>
 * </ul>
 */
@Component
public class RewardCalculator {

    private static final BigDecimal FIFTY = new BigDecimal("50");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final BigDecimal TWO = new BigDecimal("2");

    /**
     * Calculates reward points for a single transaction amount.
     *
     * @param amount transaction purchase amount
     * @return reward points earned for the transaction
     */
    public int calculatePoints(BigDecimal amount) {

        if (amount == null || amount.compareTo(FIFTY) <= 0) {
            return 0;
        }

        if (amount.compareTo(ONE_HUNDRED) <= 0) {
            return amount
                    .subtract(FIFTY)
                    .intValue();
        }

        return 50 + amount
                .subtract(ONE_HUNDRED)
                .multiply(TWO)
                .intValue();
    }
}