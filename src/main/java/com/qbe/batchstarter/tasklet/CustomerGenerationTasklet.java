package com.qbe.batchstarter.tasklet;

import com.qbe.batchstarter.context.GeneratorContext;
import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.generator.CustomerFileGenerator;
import com.qbe.batchstarter.properties.CustomerGeneratorProperties;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.writer.CsvCustomerWriter;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerGenerationTasklet implements Tasklet {

    private final GeneratorContext generatorContext;
    private final CustomerFileGenerator customerGenerator;
    private final CsvCustomerWriter customerWriter;

    private final GeneratorProperties generatorProperties;
    private final CustomerGeneratorProperties customerProperties;

    @Override
    public @Nullable RepeatStatus execute(@NotNull StepContribution contribution, @NonNull ChunkContext chunkContext)
            throws Exception {
        log.info("Starting customer generation...");

        Path outputDirectory = Path.of(generatorProperties.outputDirectory());
        Files.createDirectories(outputDirectory);

        List<CustomerDto> customers = customerGenerator.generate(customerProperties.count());
        customerWriter.write(customers, outputDirectory.resolve("customers.csv"));
        generatorContext.setCustomers(customers);

        log.info("{} customers generated", customers.size());

        contribution.incrementWriteCount(customers.size());
        return RepeatStatus.FINISHED;
    }
}
