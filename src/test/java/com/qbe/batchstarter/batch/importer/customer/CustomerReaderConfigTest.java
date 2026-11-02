package com.qbe.batchstarter.batch.importer.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;

class CustomerReaderConfigTest {

    @Test
    void shouldCreateCustomerReader() {
        GeneratorProperties properties = mock(GeneratorProperties.class);
        when(properties.outputDirectory()).thenReturn("/tmp/");
        when(properties.customerFileName()).thenReturn("customers.csv");

        CustomerReaderConfig config = new CustomerReaderConfig(properties);
        FlatFileItemReader<CustomerDto> reader = config.customerReader();

        assertThat(reader).isNotNull();
    }
}
