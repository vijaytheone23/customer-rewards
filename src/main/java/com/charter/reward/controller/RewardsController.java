package com.charter.reward.controller;

import com.charter.reward.model.RewardResponse;
import com.charter.reward.service.RewardsService;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST controller for customer reward operations.
 *
 * <p>Provides endpoints for retrieving monthly and total reward points
 * for a customer over a requested date range.</p>
 */
@RestController
@Validated
@RequestMapping("/api/v1/customers")
public class RewardsController {

    private final RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Calculates rewards for a customer within the specified date range.
     *
     * @param customerId customer identifier
     * @param startDate inclusive start date
     * @param endDate inclusive end date
     * @return customer reward response
     */
    @GetMapping("/{customerId}/rewards")
    public RewardResponse getRewards(
            @PathVariable
            @Pattern(regexp = "C\\d{3}", message = "Customer ID must be in format C001")
            String customerId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return rewardsService.calculateRewards(
                customerId,
                startDate,
                endDate);
    }
}