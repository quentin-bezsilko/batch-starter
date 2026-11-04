package com.qbe.batchstarter.batch.reporting.bestclients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.dto.BestClientReportDto;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

class BestClientReportJobConfigTest {

    private final BestClientReportJobConfig config = new BestClientReportJobConfig();

    @Test
    void shouldCreateStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();
        ListItemReader<BestClientReportDto> reader = mock(ListItemReader.class);
        FlatFileItemWriter<BestClientReportDto> writer = mock(FlatFileItemWriter.class);

        Step step = config.bestClientReportStep(jobRepository, transactionManager, reader, writer);
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("bestClientReportStep");
    }
}
