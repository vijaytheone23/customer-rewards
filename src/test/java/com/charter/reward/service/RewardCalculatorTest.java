package com.charter.reward.service;

import com.charter.reward.service.RewardCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewardCalculatorTest {

    private final RewardCalculator rewardCalculator =
            new RewardCalculator();

    @Test
    void shouldReturnZeroPointsForAmountBelow50() {

        assertEquals(
                0,
                rewardCalculator.calculatePoints(
                        new BigDecimal("40.00")));
    }

    @Test
    void shouldReturnZeroPointsForAmount50() {

        assertEquals(
                0,
                rewardCalculator.calculatePoints(
                        new BigDecimal("50.00")));
    }

    @Test
    void shouldReturn25PointsForAmount75() {

        assertEquals(
                25,
                rewardCalculator.calculatePoints(
                        new BigDecimal("75.00")));
    }

    @Test
    void shouldReturn50PointsForAmount100() {

        assertEquals(
                50,
                rewardCalculator.calculatePoints(
                        new BigDecimal("100.00")));
    }

    @Test
    void shouldReturn90PointsForAmount120() {

        assertEquals(
                90,
                rewardCalculator.calculatePoints(
                        new BigDecimal("120.00")));
    }

    @Test
    void shouldReturn250PointsForAmount200() {

        assertEquals(
                250,
                rewardCalculator.calculatePoints(
                        new BigDecimal("200.00")));
    }

    @Test
    void shouldReturnZeroPointsForNullAmount() {

        assertEquals(
                0,
                rewardCalculator.calculatePoints(null));
    }

    @Test
    void shouldReturnZeroPointsForNegativeAmount() {

        assertEquals(
                0,
                rewardCalculator.calculatePoints(
                        new BigDecimal("-10.00")));
    }
}