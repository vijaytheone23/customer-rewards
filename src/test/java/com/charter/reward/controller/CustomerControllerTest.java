package com.charter.reward.controller;

import com.charter.reward.model.Customer;
import com.charter.reward.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerRepository customerRepository;

    @Test
    void shouldReturnAllCustomers() throws Exception {

        when(customerRepository.findAll())
                .thenReturn(Arrays.asList(
                        new Customer("C001", "Customer ONE"),
                        new Customer("C002", "Customer TWO"),
                        new Customer("C003", "Customer THREE")
                ));

        mockMvc.perform(
                        get("/api/v1/customers")
                                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].customerId").value("C001"))
                .andExpect(jsonPath("$[0].customerName").value("Customer ONE"));
    }
}