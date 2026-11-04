package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.dto.FraudReportDto;
import com.qbe.batchstarter.entity.FraudAlertEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FraudAlertRepository extends JpaRepository<FraudAlertEntity, Long> {

    @Query(
            """
        select new com.qbe.batchstarter.dto.FraudReportDto(
            f.fraudType,
            count(f),
            coalesce(sum(f.amount), 0)
        )
        from FraudAlertEntity f
        group by f.fraudType
        order by count(f) desc
        """)
    List<FraudReportDto> buildFraudReport();
}
