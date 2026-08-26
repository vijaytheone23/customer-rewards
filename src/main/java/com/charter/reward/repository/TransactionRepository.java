package com.charter.reward.repository;

import com.charter.reward.model.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository abstraction for retrieving customer transactions.
 *
 * <p>The transaction retrieval method returns a CompletableFuture to
 * simulate asynchronous data retrieval from a remote data source.</p>
 */
public interface TransactionRepository {

    /**
     * Retrieves transactions for a customer within the specified date range.
     *
     * @param customerId unique customer identifier
     * @param startDate inclusive start date
     * @param endDate inclusive end date
     * @return matching transactions
     */
    List<Transaction> findByCustomerIdAndDateRange(
            String customerId,
            LocalDate startDate,
            LocalDate endDate);
}