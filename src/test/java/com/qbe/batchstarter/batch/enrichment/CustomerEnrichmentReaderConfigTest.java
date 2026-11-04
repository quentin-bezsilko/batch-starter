package com.qbe.batchstarter.batch.enrichment;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;

class CustomerEnrichmentReaderConfigTest {

    private final BatchProperties batchProperties = Mockito.mock(BatchProperties.class);

    @Test
    void shouldCreateReader() {
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);

        Mockito.when(batchProperties.chunkSize()).thenReturn(100);
        CustomerEnrichmentReaderConfig config = new CustomerEnrichmentReaderConfig(batchProperties);

        JpaPagingItemReader<CustomerEntity> reader = config.customerEnrichmentReader(entityManagerFactory);
        assertThat(reader).isNotNull();
        assertThat(reader.getName()).isEqualTo("customerReader");
    }
}
