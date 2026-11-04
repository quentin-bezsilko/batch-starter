package com.qbe.batchstarter.batch.reporting.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.CustomerReportExportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;

class CustomerReportWriterConfigTest {

    @Test
    void shouldCreateWriter() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp/");
        when(properties.customerReportFileName()).thenReturn("customers.csv");

        CustomerReportWriterConfig config = new CustomerReportWriterConfig(properties);

        FlatFileItemWriter<CustomerReportExportDto> writer = config.customerReportWriter();
        assertThat(writer).isNotNull();
    }
}
