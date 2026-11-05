package com.qbe.batchstarter.batch.reporting.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.dto.CustomerReportDto;
import com.qbe.batchstarter.dto.CustomerReportExportDto;
import com.qbe.batchstarter.listener.ObservedStepListener;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

class CustomerReportJobConfigTest {

    private final CustomerReportJobConfig config = new CustomerReportJobConfig();

    @Test
    void shouldCreateStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();
        ListItemReader<CustomerReportDto> reader = mock(ListItemReader.class);
        ItemProcessor<CustomerReportDto, CustomerReportExportDto> processor = mock(ItemProcessor.class);
        FlatFileItemWriter<CustomerReportExportDto> writer = mock(FlatFileItemWriter.class);
        ObservedStepListener listener = mock(ObservedStepListener.class);

        Step step = config.customerReportStep(jobRepository, transactionManager, reader, processor, writer, listener);
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("customerReportStep");
    }
}
