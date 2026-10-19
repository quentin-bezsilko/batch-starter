package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {}
