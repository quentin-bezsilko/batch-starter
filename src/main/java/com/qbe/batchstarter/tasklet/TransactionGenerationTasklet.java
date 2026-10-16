package com.qbe.batchstarter.tasklet;

import com.qbe.batchstarter.context.GeneratorContext;
import com.qbe.batchstarter.dto.TransactionDto;
import com.qbe.batchstarter.generator.TransactionFileGenerator;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.properties.TransactionGeneratorProperties;
import com.qbe.batchstarter.writer.CsvTransactionWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionGenerationTasklet implements Tasklet {

    private final GeneratorContext generatorContext;
    private final TransactionFileGenerator transactionFileGenerator;
    private final CsvTransactionWriter transactionWriter;

    private final GeneratorProperties generatorProperties;
    private final TransactionGeneratorProperties transactionProperties;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Starting transaction generation...");

        if (generatorContext.getAccounts() == null) {
            throw new IllegalStateException("Accounts must be generated before transactions");
        }

        Path outputDirectory = Path.of(generatorProperties.outputDirectory());
        Files.createDirectories(outputDirectory);

        List<TransactionDto> transactions =
                transactionFileGenerator.generate(generatorContext.getAccounts(), transactionProperties.count());
        transactionWriter.write(transactions, outputDirectory.resolve("transactions.csv"));

        log.info("{} transactions generated", transactions.size());

        contribution.incrementWriteCount(transactions.size());
        return RepeatStatus.FINISHED;
    }
}
