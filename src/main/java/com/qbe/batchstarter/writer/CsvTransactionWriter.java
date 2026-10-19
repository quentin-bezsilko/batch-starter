package com.qbe.batchstarter.writer;

import com.qbe.batchstarter.dto.BankTransactionDto;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CsvTransactionWriter {
    public void write(List<BankTransactionDto> transactions, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,accountId,transactionDate,transactionType,amount,merchantName,currency,transactionStatus"
                    .trim());
            writer.newLine();
            for (BankTransactionDto transaction : transactions) {
                writer.write("%d,%d,%s,%s,%s,%s,%s,%s"
                        .formatted(
                                transaction.id(),
                                transaction.accountId(),
                                transaction.transactionDate(),
                                transaction.transactionType(),
                                transaction.amount(),
                                transaction.merchantName(),
                                transaction.currency(),
                                transaction.transactionStatus()));
                writer.newLine();
            }
        }
    }
}
