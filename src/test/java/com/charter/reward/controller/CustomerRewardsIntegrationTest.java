package com.charter.reward.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerRewardsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCalculateRewardsThroughRestEndpoint() throws Exception {

        mockMvc.perform(
                        get("/api/v1/customers/C001/rewards")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("C001"))
                .andExpect(jsonPath("$.customerName").value("Customer ONE"))
                .andExpect(jsonPath("$.totalTransactions").value(8))
                .andExpect(jsonPath("$.totalAmount").value(795.00))
                .andExpect(jsonPath("$.totalRewardPoints").value(575))
                .andExpect(jsonPath("$.monthlyRewards[0].year").value(2026))
                .andExpect(jsonPath("$.monthlyRewards[0].month").value("January"))
                .andExpect(jsonPath("$.monthlyRewards[1].month").value("February"))
                .andExpect(jsonPath("$.monthlyRewards[2].month").value("March"))
                .andExpect(jsonPath("$.transactions.length()").value(8))
                .andExpect(jsonPath("$.transactions[0].transactionId").value("T001"))
                .andExpect(jsonPath("$.transactions[0].rewardPoints").value(0));
    }
}