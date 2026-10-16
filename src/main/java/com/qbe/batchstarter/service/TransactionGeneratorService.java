package com.qbe.batchstarter.service;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.MerchantReference;
import com.qbe.batchstarter.dto.TransactionDto;
import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import com.qbe.batchstarter.properties.TransactionGeneratorProperties;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionGeneratorService {

    private final TransactionGeneratorProperties transactionGeneratorProperties;

    private final Random random = new Random();

    public TransactionDto generate(long transactionId, BankAccountDto account, MerchantReference merchant) {
        return new TransactionDto(
                transactionId,
                account.id(),
                randomTransactionDate(),
                randomTransactionType(),
                randomAmount(merchant.category()),
                merchant.merchantName(),
                account.currency(),
                randomStatus());
    }

    private TransactionType randomTransactionType() {
        int value = random.nextInt(100);
        if (value < transactionGeneratorProperties.cardPaymentRate()) {
            return TransactionType.CARD_PAYMENT;
        }
        if (value < transactionGeneratorProperties.cardPaymentRate() + transactionGeneratorProperties.transferRate()) {
            return TransactionType.TRANSFER;
        }
        if (value
                < transactionGeneratorProperties.cardPaymentRate()
                        + transactionGeneratorProperties.transferRate()
                        + transactionGeneratorProperties.directDebitRate()) {
            return TransactionType.DIRECT_DEBIT;
        }
        return TransactionType.CASH_WITHDRAWAL;
    }

    private TransactionStatus randomStatus() {
        int value = random.nextInt(100);
        if (value < 95) {
            return TransactionStatus.COMPLETED;
        }
        if (value < 99) {
            return TransactionStatus.PENDING;
        }
        return TransactionStatus.REJECTED;
    }

    private LocalDateTime randomTransactionDate() {
        return LocalDateTime.now()
                .minusDays(random.nextInt(365))
                .minusHours(random.nextInt(24))
                .minusMinutes(random.nextInt(60))
                .minusSeconds(random.nextInt(60));
    }

    private BigDecimal randomAmount(String category) {
        BigDecimal amount;
        switch (category.toUpperCase()) {
            case "FOOD" -> amount = amountBetween(5, 120);
            case "TRANSPORT" -> amount = amountBetween(2, 50);
            case "SHOPPING" -> amount = amountBetween(20, 1000);
            case "UTILITIES" -> amount = amountBetween(20, 500);
            case "ENTERTAINMENT" -> amount = amountBetween(5, 100);
            default -> amount = amountBetween(5, 300);
        }

        // 0,2% de fraudes montant élevé
        if (random.nextDouble() < 0.002) {
            amount = amountBetween(10_000, 50_000);
        }
        return amount;
    }

    private BigDecimal amountBetween(double min, double max) {
        return BigDecimal.valueOf(min + (max - min) * random.nextDouble()).setScale(2, RoundingMode.HALF_UP);
    }
}
