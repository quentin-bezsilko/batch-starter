package com.qbe.batchstarter.batch.fraud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;

class FraudAlertReaderConfigTest {

    @Test
    void shouldCreateReader() {
        BatchProperties properties = mock(BatchProperties.class);
        when(properties.chunkSize()).thenReturn(100);

        FraudAlertReaderConfig config = new FraudAlertReaderConfig(properties);
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        JpaPagingItemReader<BankTransactionEntity> reader = config.fraudReader(emf);

        assertThat(reader).isNotNull();
        assertThat(reader.getName()).isEqualTo("fraudReader");
    }
}
