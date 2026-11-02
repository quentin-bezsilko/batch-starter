package com.qbe.batchstarter.batch.importer.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.entity.CustomerEntity;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;

class CustomerWriterConfigTest {

    @Test
    void shouldCreateJpaWriter() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        CustomerWriterConfig config = new CustomerWriterConfig();

        JpaItemWriter<CustomerEntity> writer = config.customerWriter(emf);
        assertThat(writer).isNotNull();
    }
}
