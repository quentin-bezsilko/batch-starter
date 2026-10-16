package com.qbe.batchstarter.writer;

import com.qbe.batchstarter.dto.BankAccountDto;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CsvAccountWriter {

    public void write(List<BankAccountDto> accounts, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,customerId,iban,accountType,currency,balance,accountStatus,openingDate");
            writer.newLine();
            for (BankAccountDto account : accounts) {
                writer.write("%d,%d,%s,%s,%s,%s,%s,%s"
                        .formatted(
                                account.id(),
                                account.customerId(),
                                account.iban(),
                                account.accountType(),
                                account.currency(),
                                account.balance(),
                                account.accountStatus(),
                                account.openingDate()));
                writer.newLine();
            }
        }
    }
}
