package com.charter.reward.repository;

import com.charter.reward.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for customer persistence.
 */
public interface CustomerJpaRepository
        extends JpaRepository<CustomerEntity, String> {
}
