package com.qbe.batchstarter.batch.reporting.transaction;

import com.qbe.batchstarter.dto.TransactionReportDto;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TransactionReportReaderConfig {

    private final BankTransactionRepository repository;

    @Bean
    @StepScope
    public ListItemReader<TransactionReportDto> transactionReportReader() {
        List<TransactionReportDto> report = repository.buildTransactionReport();
        return new ListItemReader<>(report);
    }
}
