package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.entity.BankAccountEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {

    Optional<BankAccountEntity> findByIban(String iban);

    List<BankAccountEntity> findByCustomerId(Long id);

    @Query("""
      select coalesce(sum(a.balance), 0) from BankAccountEntity a
    """)
    BigDecimal totalBalance();
}
