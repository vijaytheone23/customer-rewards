package com.charter.reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Main entry point for the Customer Rewards Spring Boot application.
 *
 * <p>Bootstraps the Spring application context and starts the embedded
 * web server.</p>
 */
@SpringBootApplication
public class CustomerRewardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerRewardsApplication.class, args);
    }

}
