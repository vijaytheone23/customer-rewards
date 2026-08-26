package com.charter.reward.service;

import com.charter.reward.model.RewardResponse;

import java.time.LocalDate;

/**
 * Defines the business operations for calculating customer rewards.
 */
public interface RewardsService {

    /**
     * Calculates monthly and total rewards for a customer.
     *
     * @param customerId unique customer identifier
     * @param startDate inclusive start date
     * @param endDate inclusive end date
     * @return calculated customer reward information
     */
    RewardResponse calculateRewards(
            String customerId,
            LocalDate startDate,
            LocalDate endDate);
}