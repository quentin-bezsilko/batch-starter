package com.qbe.batchstarter.batch.reporting.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.TransactionReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;

class TransactionReportWriterConfigTest {

    @Test
    void shouldCreateTransactionReportWriter() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp/");
        when(properties.transactionReportFileName()).thenReturn("transaction-report.csv");

        TransactionReportWriterConfig config = new TransactionReportWriterConfig(properties);

        FlatFileItemWriter<TransactionReportDto> writer = config.transactionReportWriter();
        assertThat(writer).isNotNull();
    }
}
