package com.qbe.batchstarter.batch.importer.transaction;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import com.qbe.batchstarter.properties.GeneratorProperties;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class TransactionReaderConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemReader<BankTransactionDto> transactionReader() {

        return new FlatFileItemReaderBuilder<BankTransactionDto>()
                .name("transactionReader")
                .resource(new FileSystemResource(
                        Path.of(generatorProperties.outputDirectory(), generatorProperties.transactionFileName())))
                .linesToSkip(1)
                .delimited()
                .names(
                        "id",
                        "accountId",
                        "transactionDate",
                        "transactionType",
                        "amount",
                        "merchantName",
                        "currency",
                        "transactionStatus")
                .fieldSetMapper(fieldSet -> new BankTransactionDto(
                        fieldSet.readLong("id"),
                        fieldSet.readLong("accountId"),
                        LocalDateTime.parse(Objects.requireNonNull(fieldSet.readString("transactionDate"))),
                        TransactionType.valueOf(Objects.requireNonNull(fieldSet.readString("transactionType"))),
                        fieldSet.readBigDecimal("amount"),
                        fieldSet.readString("merchantName"),
                        fieldSet.readString("currency"),
                        TransactionStatus.valueOf(Objects.requireNonNull(fieldSet.readString("transactionStatus")))))
                .build();
    }
}
