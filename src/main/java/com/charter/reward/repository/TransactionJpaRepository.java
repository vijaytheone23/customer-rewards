package com.charter.reward.repository;

import com.charter.reward.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionJpaRepository
        extends JpaRepository<TransactionEntity, String> {

    List<TransactionEntity> findByCustomerIdAndTransactionDateBetween(
            String customerId,
            LocalDate startDate,
            LocalDate endDate);
}