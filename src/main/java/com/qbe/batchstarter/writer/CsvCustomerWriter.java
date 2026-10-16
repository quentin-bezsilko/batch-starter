package com.qbe.batchstarter.writer;

import com.qbe.batchstarter.dto.CustomerDto;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CsvCustomerWriter {

    public void write(List<CustomerDto> customers, Path path) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,firstName,lastName,birthDate,country");
            writer.newLine();
            for (CustomerDto customer : customers) {
                writer.write("%d,%s,%s,%s,%s"
                        .formatted(
                                customer.id(),
                                customer.firstName(),
                                customer.lastName(),
                                customer.birthDate(),
                                customer.country()));
                writer.newLine();
            }
        }
    }
}
