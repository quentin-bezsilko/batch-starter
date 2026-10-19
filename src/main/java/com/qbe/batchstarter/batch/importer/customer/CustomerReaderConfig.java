package com.qbe.batchstarter.batch.importer.customer;

import com.qbe.batchstarter.dto.CustomerDto;
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
public class CustomerReaderConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemReader<CustomerDto> customerReader() {
        return new FlatFileItemReaderBuilder<CustomerDto>()
                .name("customerReader")
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.customerFileName()))
                .linesToSkip(1)
                .delimited()
                .names("id", "firstName", "lastName", "birthDate", "country")
                .fieldSetMapper(fieldSet -> new CustomerDto(
                        fieldSet.readLong("id"),
                        fieldSet.readString("firstName"),
                        fieldSet.readString("lastName"),
                        LocalDate.parse(Objects.requireNonNull(fieldSet.readString("birthDate"))),
                        fieldSet.readString("country")))
                .build();
    }
}
