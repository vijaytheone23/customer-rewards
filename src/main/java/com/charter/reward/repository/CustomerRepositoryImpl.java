package com.charter.reward.repository;

import com.charter.reward.entity.CustomerEntity;
import com.charter.reward.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Database-backed implementation of the customer repository.
 */
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryImpl(
            CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public Optional<Customer> findById(String customerId) {
        return customerJpaRepository.findById(customerId)
                .map(this::toCustomer);
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll()
                .stream()
                .map(this::toCustomer)
                .toList();
    }

    private Customer toCustomer(CustomerEntity entity) {
        return new Customer(
                entity.getCustomerId(),
                entity.getCustomerName());
    }
}
