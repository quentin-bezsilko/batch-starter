package com.qbe.batchstarter.batch.enrichment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.CustomerEnrichmentDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.transaction.PlatformTransactionManager;

class CustomerEnrichmentJobConfigTest {

    @Test
    void shouldCreateCustomerEnrichmentStep() {
        PlatformTransactionManager transactionManager = Mockito.mock(PlatformTransactionManager.class);
        JpaPagingItemReader<CustomerEntity> reader = Mockito.mock(JpaPagingItemReader.class);
        CustomerEnrichmentProcessor processor = Mockito.mock(CustomerEnrichmentProcessor.class);
        FlatFileItemWriter<CustomerEnrichmentDto> writer = Mockito.mock(FlatFileItemWriter.class);

        BatchProperties properties = Mockito.mock(BatchProperties.class);
        when(properties.chunkSize()).thenReturn(100);

        CustomerEnrichmentJobConfig config =
                new CustomerEnrichmentJobConfig(transactionManager, reader, processor, writer, properties);
        JobRepository repository = Mockito.mock(JobRepository.class);

        Step step = config.customerEnrichmentStep(repository);
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("customerEnrichmentStep");
    }
}
