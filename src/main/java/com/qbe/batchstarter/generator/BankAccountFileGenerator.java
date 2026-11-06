package com.qbe.batchstarter.generator;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import com.qbe.batchstarter.service.BankAccountGeneratorService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountFileGenerator {

    private final BankAccountGeneratorService bankAccountGeneratorService;
    private final AccountGeneratorProperties accountGeneratorProperties;
    private final SecureRandom random;

    public List<BankAccountDto> generate(List<CustomerDto> customers) {
        List<BankAccountDto> accounts = new ArrayList<>();
        long accountId = 1;
        for (CustomerDto customer : customers) {
            int accountCount = accountCount();
            for (int i = 0; i < accountCount; i++) {
                String currency =
                        switch (customer.country()) {
                            case "UK" -> "GBP";
                            case "US" -> "USD";
                            default -> "EUR";
                        };
                accounts.add(
                        bankAccountGeneratorService.generate(accountId++, customer.id(), customer.country(), currency));
            }
        }
        return accounts;
    }

    private int accountCount() {
        int value = random.nextInt(100);
        int singleRate = accountGeneratorProperties.singleAccountRate();
        int doubleRate = accountGeneratorProperties.doubleAccountRate();
        if (value < singleRate) {
            return 1;
        }
        if (value < singleRate + doubleRate) {
            return 2;
        }
        return 3;
    }
}
