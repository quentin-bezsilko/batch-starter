package com.qbe.batchstarter.batch.reporting.global;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.dto.GlobalReportDto;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

class GlobalReportJobConfigTest {

    private final GlobalReportJobConfig config = new GlobalReportJobConfig();

    @Test
    void shouldCreateGlobalReportStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();
        ListItemReader<GlobalReportDto> reader = mock(ListItemReader.class);

        FlatFileItemWriter<GlobalReportDto> writer = mock(FlatFileItemWriter.class);

        Step step = config.globalReportStep(jobRepository, transactionManager, reader, writer);
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("globalReportStep");
    }
}
