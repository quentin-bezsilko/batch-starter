package com.qbe.batchstarter.batch.reporting.fraud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.FraudReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;

class FraudReportWriterConfigTest {

    @Test
    void shouldCreateFraudReportWriter() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp/");
        when(properties.fraudReportFileName()).thenReturn("fraud-report.csv");

        FraudReportWriterConfig config = new FraudReportWriterConfig(properties);

        FlatFileItemWriter<FraudReportDto> writer = config.fraudReportWriter();
        assertThat(writer).isNotNull();
    }
}
