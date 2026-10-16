package com.qbe.batchstarter.service;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountGeneratorService {

    private static final int PERCENTAGE_MAX = 100;
    private static final int IBAN_KEY_MIN = 10;
    private static final int IBAN_KEY_MAX = 90;
    private static final long IBAN_MAX_VALUE = 1_000_000_000_000_000_000L;

    private final AccountGeneratorProperties properties;
    private final Random random = new Random();

    public BankAccountDto generate(long accountId, long customerId, String country, String currency) {
        return new BankAccountDto(
                accountId,
                customerId,
                generateIban(country),
                randomAccountType(),
                currency,
                randomBalance(),
                randomStatus(),
                randomOpeningDate());
    }

    private AccountType randomAccountType() {
        int value = random.nextInt(PERCENTAGE_MAX);
        if (value < properties.checkingRate()) {
            return AccountType.CHECKING;
        }
        if (value < properties.checkingRate() + properties.savingsRate()) {
            return AccountType.SAVINGS;
        }
        return AccountType.BUSINESS;
    }

    private AccountStatus randomStatus() {
        int value = random.nextInt(PERCENTAGE_MAX);
        if (value < properties.activeRate()) {
            return AccountStatus.ACTIVE;
        }
        if (value < properties.activeRate() + properties.blockedRate()) {
            return AccountStatus.BLOCKED;
        }
        return AccountStatus.CLOSED;
    }

    private BigDecimal randomBalance() {
        double min = properties.minBalance();
        double max = properties.maxBalance();
        return BigDecimal.valueOf(min + random.nextDouble() * (max - min)).setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDate randomOpeningDate() {
        return LocalDate.now().minusDays(random.nextInt(properties.maxOpeningAgeInDays() + 1));
    }

    private String generateIban(String country) {
        long accountNumber = Math.abs(random.nextLong()) % IBAN_MAX_VALUE;
        return country + (IBAN_KEY_MIN + random.nextInt(IBAN_KEY_MAX)) + String.format("%020d", accountNumber);
    }
}
