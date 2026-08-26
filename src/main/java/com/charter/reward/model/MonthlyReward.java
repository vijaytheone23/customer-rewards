package com.charter.reward.model;

/**
 * Represents the reward summary for a customer for a specific month.
 */
public record MonthlyReward(
        int year,
        String month,
        int points
) {
}