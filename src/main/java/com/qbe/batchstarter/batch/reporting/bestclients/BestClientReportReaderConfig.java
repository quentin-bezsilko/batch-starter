package com.qbe.batchstarter.batch.reporting.bestclients;

import com.qbe.batchstarter.dto.BestClientReportDto;
import com.qbe.batchstarter.repository.CustomerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BestClientReportReaderConfig {

    private final CustomerRepository customerRepository;

    @Bean
    @StepScope
    public ListItemReader<BestClientReportDto> bestClientReportReader() {
        List<BestClientReportDto> bestClients = customerRepository.findBestClients();
        return new ListItemReader<>(bestClients.stream().limit(20).toList());
    }
}
