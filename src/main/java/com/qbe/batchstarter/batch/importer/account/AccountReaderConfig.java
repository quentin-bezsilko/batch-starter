package com.qbe.batchstarter.batch.importer.account;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.properties.GeneratorProperties;
import java.time.LocalDate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class AccountReaderConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemReader<BankAccountDto> accountReader() {
        return new FlatFileItemReaderBuilder<BankAccountDto>()
                .name("accountReader")
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.accountFileName()))
                .linesToSkip(1)
                .delimited()
                .names("id", "customerId", "iban", "accountType", "currency", "balance", "accountStatus", "openingDate")
                .fieldSetMapper(fieldSet -> new BankAccountDto(
                        fieldSet.readLong("id"),
                        fieldSet.readLong("customerId"),
                        fieldSet.readString("iban"),
                        AccountType.valueOf(fieldSet.readString("accountType")),
                        fieldSet.readString("currency"),
                        fieldSet.readBigDecimal("balance"),
                        AccountStatus.valueOf(fieldSet.readString("accountStatus")),
                        LocalDate.parse(Objects.requireNonNull(fieldSet.readString("openingDate")))))
                .build();
    }
}
