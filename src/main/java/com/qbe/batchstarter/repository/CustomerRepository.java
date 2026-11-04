package com.qbe.batchstarter.repository;

import com.qbe.batchstarter.dto.BestClientReportDto;
import com.qbe.batchstarter.dto.CustomerReportDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    @Query(
            """
    select new com.qbe.batchstarter.dto.BestClientReportDto(
        c.id,
        concat(c.firstName, concat(' ', c.lastName)),
        count(a.id),
        coalesce(sum(a.balance), 0)
    )
    from CustomerEntity c
    left join BankAccountEntity a
        on a.customer.id = c.id
    group by
        c.id,
        c.firstName,
        c.lastName
    order by
        coalesce(sum(a.balance), 0) desc
    """)
    List<BestClientReportDto> findBestClients();

    @Query(
            """
    select new com.qbe.batchstarter.dto.CustomerReportDto(
        c.id,
        concat(c.firstName, concat(' ', c.lastName)),
        c.country,
        c.birthDate,
        count(a.id),
        coalesce(sum(a.balance), 0)
    )
    from CustomerEntity c
    left join BankAccountEntity a
        on a.customer.id = c.id
    group by
        c.id,
        c.firstName,
        c.lastName,
        c.country,
        c.birthDate
    """)
    List<CustomerReportDto> findCustomerReport();
}
