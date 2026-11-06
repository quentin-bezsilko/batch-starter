package com.qbe.batchstarter.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.context.GeneratorContext;
import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.generator.BankAccountFileGenerator;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.writer.CsvAccountWriter;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

class AccountGenerationTaskletTest {

    @Test
    void shouldGenerateAccounts() throws Exception {
        GeneratorContext context = new GeneratorContext();
        context.setCustomers(List.of(new CustomerDto(1L, "John", "Doe", LocalDate.now(), "FR")));

        BankAccountFileGenerator generator = mock(BankAccountFileGenerator.class);
        CsvAccountWriter writer = mock(CsvAccountWriter.class);
        GeneratorProperties properties = mock(GeneratorProperties.class);

        when(properties.outputDirectory()).thenReturn("target/test");

        List<BankAccountDto> accounts = List.of(mock(BankAccountDto.class));
        when(generator.generate(anyList())).thenReturn(accounts);

        AccountGenerationTasklet tasklet = new AccountGenerationTasklet(context, generator, writer, properties);
        StepContribution contribution = mock(StepContribution.class);
        ChunkContext chunkContext = mock(ChunkContext.class);
        RepeatStatus status = tasklet.execute(contribution, chunkContext);

        assertThat(status).isEqualTo(RepeatStatus.FINISHED);
        assertThat(context.getAccounts()).isEqualTo(accounts);

        verify(generator).generate(context.getCustomers());
        verify(writer).write(eq(accounts), any());
        verify(contribution).incrementWriteCount(1);
    }

    @Test
    void shouldFailWhenCustomersAreMissing() {
        GeneratorContext context = new GeneratorContext();
        AccountGenerationTasklet tasklet = new AccountGenerationTasklet(
                context,
                mock(BankAccountFileGenerator.class),
                mock(CsvAccountWriter.class),
                mock(GeneratorProperties.class));

        assertThatThrownBy(() -> tasklet.execute(mock(StepContribution.class), mock(ChunkContext.class)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Customers must be generated before accounts");
    }
}
