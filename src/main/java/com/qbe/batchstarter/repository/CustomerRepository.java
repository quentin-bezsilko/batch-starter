package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {}
