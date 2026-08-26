package com.charter.reward.repository;

import com.charter.reward.entity.TransactionEntity;
import com.charter.reward.model.Transaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class TransactionRepositoryImpl
        implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;

    public TransactionRepositoryImpl(
            TransactionJpaRepository transactionJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
    }

    @Override
    public List<Transaction> findByCustomerIdAndDateRange(
            String customerId,
            LocalDate startDate,
            LocalDate endDate) {

        return transactionJpaRepository
                .findByCustomerIdAndTransactionDateBetween(
                        customerId,
                        startDate,
                        endDate)
                .stream()
                .map(this::toTransaction)
                .toList();
    }

    private Transaction toTransaction(
            TransactionEntity entity) {

        return new Transaction(
                entity.getTransactionId(),
                entity.getCustomerId(),
                entity.getTransactionDate(),
                entity.getAmount());
    }
}