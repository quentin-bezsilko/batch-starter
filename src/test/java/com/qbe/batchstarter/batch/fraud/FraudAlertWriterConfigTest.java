package com.qbe.batchstarter.batch.fraud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.entity.FraudAlertEntity;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;

class FraudAlertWriterConfigTest {

    private final FraudAlertWriterConfig config = new FraudAlertWriterConfig();

    @Test
    void shouldCreateWriter() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        JpaItemWriter<FraudAlertEntity> writer = config.fraudAlertWriter(emf);
        assertThat(writer).isNotNull();
    }
}
