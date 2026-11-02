package com.qbe.batchstarter.batch.importer.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;

class TransactionWriterConfigTest {

    @Test
    void shouldCreateJpaWriter() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        TransactionWriterConfig config = new TransactionWriterConfig();

        JpaItemWriter<BankTransactionEntity> writer = config.transactionWriter(emf);
        assertThat(writer).isNotNull();
    }
}
