package com.qbe.batchstarter.batch.enrichment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.dto.CustomerEnrichmentDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;

class CustomerEnrichmentWriterConfigTest {

    private final GeneratorProperties generatorProperties = mock(GeneratorProperties.class);

    @Test
    void shouldCreateWriter() {
        CustomerEnrichmentWriterConfig config = new CustomerEnrichmentWriterConfig(generatorProperties);
        FlatFileItemWriter<CustomerEnrichmentDto> writer = config.customerEnrichmentWriter();

        assertThat(writer).isNotNull();
        assertThat(writer.getName()).isEqualTo("customerSummaryWriter");
    }
}
