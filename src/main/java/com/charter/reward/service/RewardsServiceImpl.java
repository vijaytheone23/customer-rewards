package com.charter.reward.service;

import com.charter.reward.exception.CustomerNotFoundException;
import com.charter.reward.exception.InvalidRequestException;
import com.charter.reward.model.*;
import com.charter.reward.repository.CustomerRepository;
import com.charter.reward.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default implementation of the customer rewards service.
 *
 * <p>Coordinates customer and transaction retrieval, validates the request,
 * groups transactions by month and builds the final reward response.</p>
 */
@Service
public class RewardsServiceImpl implements RewardsService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final RewardCalculator rewardCalculator;

    private static final Logger logger =
            LoggerFactory.getLogger(RewardsServiceImpl.class);

    public RewardsServiceImpl(TransactionRepository transactionRepository,
                              CustomerRepository customerRepository,
                              RewardCalculator rewardCalculator) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.rewardCalculator = rewardCalculator;
    }


    @Override
    public RewardResponse calculateRewards(
            String customerId,
            LocalDate startDate,
            LocalDate endDate) {

        logger.info(
                "Calculating rewards for customerId={}, startDate={}, endDate={}",
                customerId,
                startDate,
                endDate);

        if (customerId == null || customerId.trim().isEmpty()) {
            throw new InvalidRequestException(
                    "Customer ID is required");
        }

        if (startDate == null || endDate == null) {
            throw new InvalidRequestException(
                    "Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidRequestException(
                    "Start date cannot be after end date");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(customerId));

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndDateRange(
                        customerId,
                        startDate,
                        endDate);

        Map<YearMonth, List<Transaction>> transactionsByMonth =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                transaction ->
                                        YearMonth.from(
                                                transaction.transactionDate())));

        List<MonthlyReward> monthlyRewards = new ArrayList<>();

        for (Map.Entry<YearMonth, List<Transaction>> entry
                : transactionsByMonth.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .toList()) {

            YearMonth month = entry.getKey();
            List<Transaction> monthlyTransactions = entry.getValue();

            BigDecimal transactionAmount =
                    monthlyTransactions.stream()
                            .map(Transaction::amount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

            int rewardPoints =
                    monthlyTransactions.stream()
                            .mapToInt(transaction ->
                                    rewardCalculator.calculatePoints(transaction.amount()))
                            .sum();

            String monthName = month.getMonth()
                    .getDisplayName(
                            java.time.format.TextStyle.FULL,
                            java.util.Locale.ENGLISH);

            monthlyRewards.add(
                    new MonthlyReward(
                            month.getYear(),
                            monthName,
                            rewardPoints));
        }

        int totalRewardPoints =
                monthlyRewards.stream()
                        .mapToInt(MonthlyReward::points)
                        .sum();

        BigDecimal totalAmount =
                transactions.stream()
                        .map(Transaction::amount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<RewardTransaction> rewardTransactions =
                transactions.stream()
                        .map(transaction ->
                                new RewardTransaction(
                                        transaction.transactionId(),
                                        transaction.transactionDate(),
                                        transaction.amount(),
                                        rewardCalculator.calculatePoints(
                                                transaction.amount())))
                        .toList();

        logger.info(
                "Found {} transactions for customerId={}",
                transactions.size(),
                customerId);

        logger.info(
                "Reward calculation completed for customerId={}, totalPoints={}",
                customerId,
                totalRewardPoints);

        return new RewardResponse(
                customerId,
                customer.customerName(),
                startDate,
                endDate,
                monthlyRewards,
                rewardTransactions,
                transactions.size(),
                totalAmount,
                totalRewardPoints
        );
    }
}