package com.qbe.batchstarter.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.context.GeneratorContext;
import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.generator.CustomerFileGenerator;
import com.qbe.batchstarter.properties.CustomerGeneratorProperties;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.writer.CsvCustomerWriter;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

class CustomerGenerationTaskletTest {

    @Test
    void shouldGenerateCustomers() throws Exception {
        GeneratorContext context = new GeneratorContext();

        CustomerFileGenerator generator = mock(CustomerFileGenerator.class);
        CsvCustomerWriter writer = mock(CsvCustomerWriter.class);
        GeneratorProperties generatorProperties = mock(GeneratorProperties.class);
        CustomerGeneratorProperties customerProperties = mock(CustomerGeneratorProperties.class);

        when(generatorProperties.outputDirectory()).thenReturn("target/test");
        when(customerProperties.count()).thenReturn(2);

        List<CustomerDto> customers = List.of(
                new CustomerDto(1L, "John", "Doe", LocalDate.now(), "FR"),
                new CustomerDto(2L, "Jane", "Doe", LocalDate.now(), "FR"));

        when(generator.generate(2)).thenReturn(customers);

        CustomerGenerationTasklet tasklet =
                new CustomerGenerationTasklet(context, generator, writer, generatorProperties, customerProperties);

        StepContribution contribution = mock(StepContribution.class);
        ChunkContext chunkContext = mock(ChunkContext.class);

        RepeatStatus status = tasklet.execute(contribution, chunkContext);

        assertThat(status).isEqualTo(RepeatStatus.FINISHED);
        assertThat(context.getCustomers()).containsExactlyElementsOf(customers);

        verify(generator).generate(2);
        verify(writer).write(eq(customers), ArgumentMatchers.any());

        verify(contribution).incrementWriteCount(2);
    }
}
