package com.qbe.batchstarter.batch.reporting.customer;

import com.qbe.batchstarter.dto.CustomerReportDto;
import com.qbe.batchstarter.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomerReportReaderConfig {

    private final CustomerRepository repository;

    @Bean
    @StepScope
    public ListItemReader<CustomerReportDto> customerReportReader() {
        List<CustomerReportDto> reports = repository.findCustomerReport();
        return new ListItemReader<>(reports);
    }
}
