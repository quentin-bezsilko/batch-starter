package com.qbe.batchstarter.batch.importer.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.batch.importer.RangeReader;
import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;

class TransactionReaderConfigTest {

    @Test
    void shouldCreateTransactionReader() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp");
        when(properties.transactionFileName()).thenReturn("transactions.csv");

        TransactionReaderConfig config = new TransactionReaderConfig(properties);
        RangeReader<BankTransactionDto> reader = config.transactionReader(1, 100);

        assertThat(reader).isNotNull();
    }
}
