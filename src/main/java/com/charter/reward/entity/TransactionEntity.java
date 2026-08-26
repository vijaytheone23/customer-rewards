package com.charter.reward.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    private String transactionId;

    private String customerId;

    private LocalDate transactionDate;

    private BigDecimal amount;

    protected TransactionEntity() {
    }

    public TransactionEntity(
            String transactionId,
            String customerId,
            LocalDate transactionDate,
            BigDecimal amount) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}