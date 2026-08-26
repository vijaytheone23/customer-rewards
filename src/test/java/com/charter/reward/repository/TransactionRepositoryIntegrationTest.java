package com.charter.reward.repository;

import com.charter.reward.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionRepositoryIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void shouldRetrieveTransactionsFromDatabase() {

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndDateRange(
                        "C001",
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 3, 31));

        assertEquals(8, transactions.size());
    }
}