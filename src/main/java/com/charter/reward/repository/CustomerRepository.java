package com.charter.reward.repository;

import com.charter.reward.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Repository abstraction for retrieving customer information.
 *
 * <p>The interface separates customer data access from business logic
 * and allows the underlying data source to be changed without modifying
 * the service layer.</p>
 */

public interface CustomerRepository {

    /**
     * Finds a customer using the customer ID.
     *
     * @param customerId unique customer identifier
     * @return customer information if the customer exists
     */

    Optional<Customer> findById(String customerId);

    List<Customer> findAll();
}