package com.qbe.batchstarter.batch.importer.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.transaction.PlatformTransactionManager;

class CustomerImportJobConfigTest {

    @Test
    void shouldCreateCustomerImportJob() {
        CustomerImportJobConfig config = new CustomerImportJobConfig(
                mock(PlatformTransactionManager.class),
                mock(FlatFileItemReader.class),
                mock(CustomerProcessorConfig.class),
                mock(JpaItemWriter.class),
                mock(BatchProperties.class));
        Job job = config.customerImportJob(mock(JobRepository.class), mock(Step.class));

        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("customerImportJob");
    }
}
