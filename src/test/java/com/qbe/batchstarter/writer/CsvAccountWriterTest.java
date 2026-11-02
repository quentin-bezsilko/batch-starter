package com.qbe.batchstarter.writer;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvAccountWriterTest {

    private final CsvAccountWriter writer = new CsvAccountWriter();

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteAccountsToCsv() throws Exception {
        Path file = tempDir.resolve("accounts.csv");

        List<BankAccountDto> accounts = List.of(new BankAccountDto(
                1L,
                10L,
                "FR761234567890",
                AccountType.CHECKING,
                "EUR",
                BigDecimal.valueOf(1500.50),
                AccountStatus.ACTIVE,
                LocalDate.of(2020, 1, 1)));

        writer.write(accounts, file);

        List<String> lines = Files.readAllLines(file);
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0)).isEqualTo("id,customerId,iban,accountType,currency,balance,accountStatus,openingDate");
        assertThat(lines.get(1)).isEqualTo("1,10,FR761234567890,CHECKING,EUR,1500.5,ACTIVE,2020-01-01");
    }
}
