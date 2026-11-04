package com.qbe.batchstarter.batch.fraud;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.entity.FraudAlertEntity;
import com.qbe.batchstarter.enums.FraudType;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FraudAlertProcessor implements ItemProcessor<BankTransactionEntity, FraudAlertEntity> {

    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = BigDecimal.valueOf(20_000);
    private static final BigDecimal NEGATIVE_BALANCE_THRESHOLD = BigDecimal.valueOf(-5_000);

    private final BankTransactionRepository transactionRepository;

    private final Map<Long, List<BankTransactionEntity>> accountTransactionsCache = new ConcurrentHashMap<>();

    @Override
    public FraudAlertEntity process(BankTransactionEntity transaction) {

        // 1 - Montant de transaction élevé
        if (transaction.getAmount() != null && transaction.getAmount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0) {
            return buildAlert(transaction, FraudType.HIGH_AMOUNT, "Amount exceeds threshold");
        }

        // 2 - Compte fortement débiteur
        if (transaction.getAccount() != null
                && transaction.getAccount().getBalance() != null
                && transaction.getAccount().getBalance().compareTo(NEGATIVE_BALANCE_THRESHOLD) < 0) {
            return buildAlert(transaction, FraudType.NEGATIVE_BALANCE, "Account balance below threshold");
        }

        if (transaction.getAccount() == null) {
            return null;
        }

        Long accountId = transaction.getAccount().getId();
        List<BankTransactionEntity> accountTransactions =
                accountTransactionsCache.computeIfAbsent(accountId, transactionRepository::findByAccountId);

        // 3 - Paiements répétés
        if (transaction.getAmount() != null) {
            long duplicatePayments = accountTransactions.stream()
                    .filter(t -> t.getAmount() != null)
                    .filter(t -> t.getAmount().compareTo(transaction.getAmount()) == 0)
                    .count();

            if (duplicatePayments >= 5) {
                return buildAlert(transaction, FraudType.DUPLICATE_PAYMENT, "Repeated identical amount detected");
            }
        }

        // 4 - Transactions rapprochées
        if (transaction.getTransactionDate() != null) {
            LocalDateTime fiveMinutesAgo = transaction.getTransactionDate().minusMinutes(5);
            long recentTransactions = accountTransactions.stream()
                    .filter(t -> t.getTransactionDate() != null)
                    .filter(t -> !t.getTransactionDate().isBefore(fiveMinutesAgo))
                    .count();

            if (recentTransactions >= 5) {
                return buildAlert(transaction, FraudType.RAPID_TRANSACTIONS, "Too many transactions in a short period");
            }
        }

        return null;
    }

    private FraudAlertEntity buildAlert(BankTransactionEntity transaction, FraudType fraudType, String reason) {
        return FraudAlertEntity.builder()
                .transactionId(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .fraudType(fraudType.name())
                .amount(transaction.getAmount())
                .reason(reason)
                .detectionDate(LocalDateTime.now())
                .build();
    }
}
