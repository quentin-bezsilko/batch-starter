package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransactionEntity, Long> {}
