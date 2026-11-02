package com.qbe.batchstarter.batch.importer.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.entity.BankAccountEntity;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;

public class AccountWriterConfigTest {

    @Test
    void shouldCreateJpaWriter() {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        AccountWriterConfig config = new AccountWriterConfig();

        JpaItemWriter<BankAccountEntity> writer = config.accountWriter(emf);
        assertThat(writer).isNotNull();
    }
}
