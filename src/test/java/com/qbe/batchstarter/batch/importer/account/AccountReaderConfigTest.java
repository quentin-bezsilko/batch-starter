package com.qbe.batchstarter.batch.importer.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;

public class AccountReaderConfigTest {

    @Test
    void shouldCreateReader() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp/");
        when(properties.accountFileName()).thenReturn("accounts.csv");

        AccountReaderConfig config = new AccountReaderConfig(properties);
        FlatFileItemReader<BankAccountDto> reader = config.accountReader();
        assertThat(reader).isNotNull();
    }
}
