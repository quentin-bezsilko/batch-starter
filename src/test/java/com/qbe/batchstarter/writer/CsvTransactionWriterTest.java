package com.qbe.batchstarter.writer;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvTransactionWriterTest {

    private final CsvTransactionWriter writer = new CsvTransactionWriter();

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteTransactionsToCsv() throws Exception {
        Path file = tempDir.resolve("transactions.csv");

        LocalDateTime date = LocalDateTime.of(2024, 1, 1, 10, 30, 0);

        List<BankTransactionDto> transactions = List.of(new BankTransactionDto(
                1L,
                "FR123456",
                date,
                TransactionType.TRANSFER,
                BigDecimal.valueOf(125.50),
                "Amazon",
                "EUR",
                TransactionStatus.COMPLETED));

        writer.write(transactions, file);

        List<String> lines = Files.readAllLines(file);
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0))
                .isEqualTo(
                        "id,accountIban,transactionDate,transactionType,amount,merchantName,currency,transactionStatus");
        assertThat(lines.get(1)).isEqualTo("1,FR123456,2024-01-01T10:30,TRANSFER,125.5,Amazon,EUR,COMPLETED");
    }
}
