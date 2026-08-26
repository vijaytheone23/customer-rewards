package com.charter.reward.controller;

import com.charter.reward.controller.RewardsController;
import com.charter.reward.exception.CustomerNotFoundException;
import com.charter.reward.exception.GlobalExceptionHandler;
import com.charter.reward.exception.InvalidRequestException;
import com.charter.reward.model.MonthlyReward;
import com.charter.reward.model.RewardResponse;
import com.charter.reward.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web layer tests for CustomerRewardsControllerTest.
 *
 * <p>Verifies REST API responses for successful requests and
 * error scenarios.</p>
 */
@WebMvcTest(controllers = RewardsController.class)
@Import(GlobalExceptionHandler.class)
class CustomerRewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardsService rewardsService;

    @Test
    void shouldReturnCustomerRewards() throws Exception {

        RewardResponse response = new RewardResponse(
                "C001",
                "Customer ONE",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 3, 31),
                Collections.emptyList(),
                Collections.emptyList(),
                2,
                new BigDecimal("195.00"),
                115
        );

        when(rewardsService.calculateRewards(
                eq("C001"),
                eq(LocalDate.of(2026, 1, 1)),
                eq(LocalDate.of(2026, 3, 31))))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/customers/C001/rewards")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-03-31")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId")
                        .value("C001"))
                .andExpect(jsonPath("$.customerName")
                        .value("Customer ONE"))
                .andExpect(jsonPath("$.totalTransactions")
                        .value(2))
                .andExpect(jsonPath("$.totalRewardPoints")
                        .value(115));
    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {

        when(rewardsService.calculateRewards(
                eq("C999"),
                eq(LocalDate.of(2026, 1, 1)),
                eq(LocalDate.of(2026, 3, 31))))
                .thenThrow(
                        new CustomerNotFoundException("C999"));

        mockMvc.perform(
                        get("/api/v1/customers/C999/rewards")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-03-31")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Customer Not Found"));
    }

    @Test
    void shouldReturn400ForInvalidDateRange() throws Exception {

        when(rewardsService.calculateRewards(
                eq("C001"),
                eq(LocalDate.of(2026, 3, 31)),
                eq(LocalDate.of(2026, 1, 1))))
                .thenThrow(
                        new InvalidRequestException(
                                "Start date cannot be after end date"));

        mockMvc.perform(
                        get("/api/v1/customers/C001/rewards")
                                .param("startDate", "2026-03-31")
                                .param("endDate", "2026-01-01")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error")
                        .value("Invalid Request"));
    }

    @Test
    void shouldReturn400ForInvalidDateFormat() throws Exception {

        mockMvc.perform(
                        get("/api/v1/customers/C001/rewards")
                                .param("startDate", "wrong-date")
                                .param("endDate", "2026-03-31")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenStartDateIsMissing() throws Exception {

        mockMvc.perform(
                        get("/api/v1/customers/C001/rewards")
                                .param("endDate", "2026-03-31")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenEndDateIsMissing() throws Exception {

        mockMvc.perform(
                        get("/api/v1/customers/C001/rewards")
                                .param("startDate", "2026-01-01")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForInvalidCustomerIdFormat() throws Exception {

        mockMvc.perform(
                        get("/api/v1/customers/ABC/rewards")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-03-31")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForInvalidCustomerIdLength() throws Exception {

        mockMvc.perform(
                        get("/api/v1/customers/C01/rewards")
                                .param("startDate", "2026-01-01")
                                .param("endDate", "2026-03-31")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}