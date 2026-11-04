package com.qbe.batchstarter.batch.reporting.global;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.GlobalReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;

class GlobalReportWriterConfigTest {

    @Test
    void shouldCreateGlobalReportWriter() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp/");
        when(properties.globalReportFileName()).thenReturn("global-report.csv");

        GlobalReportWriterConfig config = new GlobalReportWriterConfig(properties);

        FlatFileItemWriter<GlobalReportDto> writer = config.globalReportWriter();
        assertThat(writer).isNotNull();
    }
}
