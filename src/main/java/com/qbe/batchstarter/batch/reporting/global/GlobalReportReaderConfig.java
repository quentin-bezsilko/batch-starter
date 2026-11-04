package com.qbe.batchstarter.batch.reporting.global;

import com.qbe.batchstarter.dto.GlobalReportDto;
import com.qbe.batchstarter.repository.BankAccountRepository;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import com.qbe.batchstarter.repository.CustomerRepository;
import com.qbe.batchstarter.repository.FraudAlertRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GlobalReportReaderConfig {

    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;
    private final CustomerRepository customerRepository;
    private final FraudAlertRepository fraudAlertRepository;

    @Bean
    @StepScope
    public ListItemReader<GlobalReportDto> globalReportReader() {
        GlobalReportDto report = new GlobalReportDto(
                LocalDate.now(),
                customerRepository.count(),
                bankAccountRepository.count(),
                bankTransactionRepository.count(),
                fraudAlertRepository.count(),
                bankAccountRepository.totalBalance());
        return new ListItemReader<>(List.of(report));
    }
}
