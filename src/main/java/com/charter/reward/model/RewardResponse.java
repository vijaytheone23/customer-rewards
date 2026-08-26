package com.charter.reward.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents the complete reward calculation response for a customer.
 *
 * <p>The response contains customer information, requested date range,
 * monthly reward summaries and overall transaction and reward totals.</p>
 */

public record RewardResponse(
        String customerId,
        String customerName,
        LocalDate startDate,
        LocalDate endDate,
        List<MonthlyReward> monthlyRewards,
        List<RewardTransaction> transactions,
        int totalTransactions,
        BigDecimal totalAmount,
        int totalRewardPoints
) {
}
