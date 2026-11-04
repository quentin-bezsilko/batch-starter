package com.qbe.batchstarter.batch.reporting.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.dto.TransactionReportDto;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

class TransactionReportJobConfigTest {

    private final TransactionReportJobConfig config = new TransactionReportJobConfig();

    @Test
    void shouldCreateTransactionReportStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();
        ListItemReader<TransactionReportDto> reader = mock(ListItemReader.class);
        FlatFileItemWriter<TransactionReportDto> writer = mock(FlatFileItemWriter.class);

        Step step = config.transactionReportStep(jobRepository, transactionManager, reader, writer);
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("transactionReportStep");
    }
}
