package com.qbe.batchstarter.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.context.GeneratorContext;
import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.generator.BankTransactionFileGenerator;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.properties.TransactionGeneratorProperties;
import com.qbe.batchstarter.writer.CsvTransactionWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

class TransactionGenerationTaskletTest {

    @Test
    void shouldGenerateTransactions() throws Exception {
        GeneratorContext context = new GeneratorContext();

        List<BankAccountDto> accounts = List.of(new BankAccountDto(
                1L,
                1L,
                "FR761234567890",
                AccountType.CHECKING,
                "EUR",
                BigDecimal.TEN,
                AccountStatus.ACTIVE,
                LocalDate.now()));

        context.setAccounts(accounts);

        BankTransactionFileGenerator generator = mock(BankTransactionFileGenerator.class);
        CsvTransactionWriter writer = mock(CsvTransactionWriter.class);
        GeneratorProperties generatorProperties = mock(GeneratorProperties.class);
        TransactionGeneratorProperties transactionProperties = mock(TransactionGeneratorProperties.class);

        when(generatorProperties.outputDirectory()).thenReturn("target/test");
        when(transactionProperties.count()).thenReturn(2);

        List<BankTransactionDto> transactions = List.of(
                new BankTransactionDto(
                        1L, "FR761234567890", LocalDateTime.now(), null, BigDecimal.ONE, "Amazon", "EUR", null),
                new BankTransactionDto(
                        2L, "FR761234567890", LocalDateTime.now(), null, BigDecimal.TEN, "Carrefour", "EUR", null));

        when(generator.generate(accounts, 2)).thenReturn(transactions);

        TransactionGenerationTasklet tasklet = new TransactionGenerationTasklet(
                context, generator, writer, generatorProperties, transactionProperties);

        StepContribution contribution = mock(StepContribution.class);
        ChunkContext chunkContext = mock(ChunkContext.class);

        RepeatStatus result = tasklet.execute(contribution, chunkContext);
        assertThat(result).isEqualTo(RepeatStatus.FINISHED);
        verify(generator).generate(accounts, 2);
        verify(writer).write(eq(transactions), any());
        verify(contribution).incrementWriteCount(2);
    }

    @Test
    void shouldThrowExceptionWhenAccountsAreMissing() {
        GeneratorContext context = new GeneratorContext();

        TransactionGenerationTasklet tasklet = new TransactionGenerationTasklet(
                context,
                mock(BankTransactionFileGenerator.class),
                mock(CsvTransactionWriter.class),
                mock(GeneratorProperties.class),
                mock(TransactionGeneratorProperties.class));

        assertThatThrownBy(() -> tasklet.execute(mock(StepContribution.class), mock(ChunkContext.class)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Accounts must be generated before transactions");
    }
}
