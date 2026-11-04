package com.qbe.batchstarter.batch.reporting.fraud;

import com.qbe.batchstarter.dto.FraudReportDto;
import com.qbe.batchstarter.repository.FraudAlertRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FraudReportReaderConfig {

    private final FraudAlertRepository fraudAlertRepository;

    @Bean
    @StepScope
    public ListItemReader<FraudReportDto> fraudReportReader() {
        List<FraudReportDto> report = fraudAlertRepository.buildFraudReport();
        return new ListItemReader<>(report);
    }
}
