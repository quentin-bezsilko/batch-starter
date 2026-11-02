package com.qbe.batchstarter.writer;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.dto.CustomerDto;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvCustomerWriterTest {

    private final CsvCustomerWriter writer = new CsvCustomerWriter();

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteCustomersToCsv() throws Exception {
        Path file = tempDir.resolve("customers.csv");
        List<CustomerDto> customers = List.of(new CustomerDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "FR"));

        writer.write(customers, file);

        List<String> lines = Files.readAllLines(file);
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0)).isEqualTo("id,firstName,lastName,birthDate,country");
        assertThat(lines.get(1)).isEqualTo("1,John,Doe,1990-01-01,FR");
    }
}
