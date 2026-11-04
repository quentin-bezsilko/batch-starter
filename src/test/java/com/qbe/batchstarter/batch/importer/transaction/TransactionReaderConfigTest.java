package com.qbe.batchstarter.batch.importer.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.batch.importer.RangeReader;
import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionReaderConfigTest {

    private TransactionReaderConfig config;

    @BeforeEach
    void setUp() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp");
        when(properties.transactionFileName()).thenReturn("transactions.csv");

        config = new TransactionReaderConfig(properties);
    }

    @Test
    void shouldCreateTransactionReader() {
        RangeReader<BankTransactionDto> reader = config.transactionReader(1, 100);
        assertThat(reader).isNotNull();
    }

    @Test
    void shouldCreateTransactionReaderForSingleLine() {
        RangeReader<BankTransactionDto> reader = config.transactionReader(10, 10);
        assertThat(reader).isNotNull();
    }

    @Test
    void shouldCreateTransactionReaderForPartition() {
        Integer startLine = 25001;
        Integer endLine = 50000;

        RangeReader<BankTransactionDto> reader = config.transactionReader(startLine, endLine);
        assertThat(reader).isNotNull();
    }
}
