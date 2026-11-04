package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.dto.TransactionReportDto;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankTransactionRepository extends JpaRepository<BankTransactionEntity, Long> {

    List<BankTransactionEntity> findByAccountId(Long id);

    @Query(
            """
    select new com.qbe.batchstarter.dto.TransactionReportDto(
        t.transactionType,
        count(t),
        sum(abs(t.amount)),
        avg(abs(t.amount))
    )
    from BankTransactionEntity t
    group by t.transactionType
    order by count(t) desc
    """)
    List<TransactionReportDto> buildTransactionReport();
}
