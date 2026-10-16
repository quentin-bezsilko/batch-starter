package com.qbe.batchstarter.tasklet;

import com.qbe.batchstarter.context.GeneratorContext;
import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.generator.AccountFileGenerator;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.writer.CsvAccountWriter;
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
@RequiredArgsConstructor
@Slf4j
public class AccountGenerationTasklet implements Tasklet {

    private final GeneratorContext generatorContext;
    private final AccountFileGenerator accountGenerator;
    private final CsvAccountWriter accountWriter;

    private final GeneratorProperties generatorProperties;

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Starting account generation...");

        if (generatorContext.getCustomers() == null) {
            throw new IllegalStateException("Customers must be generated before accounts");
        }

        Path outputDirectory = Path.of(generatorProperties.outputDirectory());
        Files.createDirectories(outputDirectory);

        List<BankAccountDto> accounts = accountGenerator.generate(generatorContext.getCustomers());
        accountWriter.write(accounts, outputDirectory.resolve("accounts.csv"));
        generatorContext.setAccounts(accounts);

        log.info("{} accounts generated", accounts.size());

        contribution.incrementWriteCount(accounts.size());
        return RepeatStatus.FINISHED;
    }
}
