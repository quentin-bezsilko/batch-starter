package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.entity.BankAccountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {

    Optional<BankAccountEntity> findByIban(String iban);
}
