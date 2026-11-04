package com.qbe.batchstarter.batch.reporting.customer;

import com.qbe.batchstarter.dto.CustomerReportDto;
import com.qbe.batchstarter.dto.CustomerReportExportDto;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerReportProcessor implements ItemProcessor<CustomerReportDto, CustomerReportExportDto> {

    @Override
    public CustomerReportExportDto process(CustomerReportDto item) {
        Integer age = item.birthDate() == null
                ? null
                : Period.between(item.birthDate(), LocalDate.now()).getYears();

        return new CustomerReportExportDto(
                item.customerId(), item.customerName(), item.country(), age, item.accountCount(), item.totalBalance());
    }
}
