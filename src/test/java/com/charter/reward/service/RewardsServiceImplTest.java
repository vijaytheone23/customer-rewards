package com.charter.reward.service;

import com.charter.reward.exception.CustomerNotFoundException;
import com.charter.reward.exception.InvalidRequestException;
import com.charter.reward.model.Customer;
import com.charter.reward.model.RewardResponse;
import com.charter.reward.model.RewardTransaction;
import com.charter.reward.model.Transaction;
import com.charter.reward.repository.CustomerRepository;
import com.charter.reward.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    private RewardsServiceImpl rewardsService;

    @BeforeEach
    void setUp() {

        RewardCalculator rewardCalculator =
                new RewardCalculator();

        rewardsService = new RewardsServiceImpl(
                transactionRepository,
                customerRepository,
                rewardCalculator);
    }

    @Test
    void shouldCalculateMonthlyAndTotalRewards() {

        String customerId = "C001";

        LocalDate startDate =
                LocalDate.of(2026, 1, 1);

        LocalDate endDate =
                LocalDate.of(2026, 3, 31);

        Customer customer =
                new Customer("C001", "Customer ONE");

        Transaction transaction1 =
                new Transaction(
                        "T001",
                        "C001",
                        LocalDate.of(2026, 1, 10),
                        new BigDecimal("75.00"));

        Transaction transaction2 =
                new Transaction(
                        "T002",
                        "C001",
                        LocalDate.of(2026, 2, 10),
                        new BigDecimal("120.00"));

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        when(transactionRepository
                .findByCustomerIdAndDateRange(
                        customerId,
                        startDate,
                        endDate))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        RewardResponse response =
                rewardsService.calculateRewards(
                        customerId,
                        startDate,
                        endDate);

        assertEquals("C001", response.customerId());
        assertEquals("Customer ONE", response.customerName());
        assertEquals(2, response.totalTransactions());
        assertEquals(
                new BigDecimal("195.00"),
                response.totalAmount());

        // $75 = 25 points
        // $120 = 90 points
        // Total = 115
        assertEquals(
                115,
                response.totalRewardPoints());

        assertEquals(
                2,
                response.monthlyRewards().size());

        assertEquals("January", response.monthlyRewards().get(0).month());
        assertEquals(2026, response.monthlyRewards().get(0).year());
        assertEquals("February", response.monthlyRewards().get(1).month());
        assertEquals(2026, response.monthlyRewards().get(1).year());

        assertEquals(2, response.transactions().size());
        RewardTransaction firstTransaction = response.transactions().get(0);
        assertEquals("T001", firstTransaction.transactionId());
        assertEquals(new BigDecimal("75.00"), firstTransaction.amount());
        assertEquals(25, firstTransaction.rewardPoints());
    }

    @Test
    void shouldSortMonthlyRewardsChronologically() {

        String customerId = "C001";
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 3, 31);

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(new Customer("C001", "Customer ONE")));

        Transaction march = new Transaction(
                "T003", "C001", LocalDate.of(2026, 3, 10),
                new BigDecimal("150.00"));
        Transaction january = new Transaction(
                "T001", "C001", LocalDate.of(2026, 1, 10),
                new BigDecimal("75.00"));
        Transaction february = new Transaction(
                "T002", "C001", LocalDate.of(2026, 2, 10),
                new BigDecimal("100.00"));

        when(transactionRepository.findByCustomerIdAndDateRange(
                customerId, startDate, endDate))
                .thenReturn(Arrays.asList(march, january, february));

        RewardResponse response = rewardsService.calculateRewards(
                customerId, startDate, endDate);

        assertEquals("January", response.monthlyRewards().get(0).month());
        assertEquals("February", response.monthlyRewards().get(1).month());
        assertEquals("March", response.monthlyRewards().get(2).month());
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {

        when(customerRepository.findById("C999"))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> rewardsService.calculateRewards(
                        "C999",
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 3, 31)));
    }

    @Test
    void shouldRejectInvalidDateRange() {

        LocalDate startDate =
                LocalDate.of(2026, 3, 31);

        LocalDate endDate =
                LocalDate.of(2026, 1, 1);

        assertThrows(
                InvalidRequestException.class,
                () -> rewardsService.calculateRewards(
                        "C001",
                        startDate,
                        endDate));
    }

    @Test
    void shouldReturnZeroRewardsWhenNoTransactionsExist() {

        String customerId = "C001";

        LocalDate startDate =
                LocalDate.of(2026, 1, 1);

        LocalDate endDate =
                LocalDate.of(2026, 3, 31);

        Customer customer =
                new Customer("C001", "Customer ONE");

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));

        when(transactionRepository
                .findByCustomerIdAndDateRange(
                        customerId,
                        startDate,
                        endDate))
                .thenReturn(Arrays.asList());

        RewardResponse response =
                rewardsService.calculateRewards(
                        customerId,
                        startDate,
                        endDate);

        assertEquals("C001", response.customerId());
        assertEquals("Customer ONE", response.customerName());
        assertEquals(0, response.totalTransactions());
        assertEquals(
                BigDecimal.ZERO,
                response.totalAmount());
        assertEquals(
                0,
                response.totalRewardPoints());
        assertEquals(
                0,
                response.monthlyRewards().size());
    }

    @Test
    void shouldRejectNullCustomerId() {

        assertThrows(
                InvalidRequestException.class,
                () -> rewardsService.calculateRewards(
                        null,
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 3, 31)));
    }

    @Test
    void shouldRejectBlankCustomerId() {

        assertThrows(
                InvalidRequestException.class,
                () -> rewardsService.calculateRewards(
                        "   ",
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 3, 31)));
    }

}