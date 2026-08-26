package com.charter.reward.repository;

import com.charter.reward.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for database-backed customer persistence.
 */
@SpringBootTest
class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldLoadCustomersFromH2Database() {
        List<Customer> customers = customerRepository.findAll();

        assertEquals(3, customers.size());
        assertTrue(customers.stream()
                .anyMatch(customer ->
                        "C001".equals(customer.customerId())
                                && "Customer ONE".equals(customer.customerName())));
    }

    @Test
    void shouldFindCustomerByIdFromH2Database() {
        var customer = customerRepository.findById("C001");

        assertTrue(customer.isPresent());
        assertEquals("Customer ONE", customer.get().customerName());
    }
}
