package com.qbe.batchstarter.batch.reporting.bestclients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BestClientReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;

class BestClientReportWriterConfigTest {

    @Test
    void shouldCreateWriter() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp/");
        when(properties.bestClientReportFileName()).thenReturn("best-clients.csv");

        BestClientReportWriterConfig config = new BestClientReportWriterConfig(properties);
        FlatFileItemWriter<BestClientReportDto> writer = config.bestClientReportWriter();

        assertThat(writer).isNotNull();
    }
}
