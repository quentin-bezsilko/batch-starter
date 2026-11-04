package com.qbe.batchstarter.batch.fraud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.entity.FraudAlertEntity;
import com.qbe.batchstarter.enums.FraudType;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FraudAlertProcessorTest {

    @Mock
    private BankTransactionRepository repository;

    private FraudAlertProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new FraudAlertProcessor(repository);
    }

    @Test
    void shouldDetectHighAmountFraud() {
        BankAccountEntity account =
                BankAccountEntity.builder().id(1L).balance(BigDecimal.ZERO).build();

        BankTransactionEntity transaction = BankTransactionEntity.builder()
                .id(10L)
                .account(account)
                .amount(BigDecimal.valueOf(25000))
                .build();

        FraudAlertEntity result = processor.process(transaction);
        assertThat(result).isNotNull();
        assertThat(result.getFraudType()).isEqualTo(FraudType.HIGH_AMOUNT.name());
    }

    @Test
    void shouldDetectNegativeBalanceFraud() {
        BankAccountEntity account = BankAccountEntity.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(-6000))
                .build();

        BankTransactionEntity transaction = BankTransactionEntity.builder()
                .id(10L)
                .account(account)
                .amount(BigDecimal.valueOf(100))
                .build();

        FraudAlertEntity result = processor.process(transaction);
        assertThat(result).isNotNull();
        assertThat(result.getFraudType()).isEqualTo(FraudType.NEGATIVE_BALANCE.name());
    }

    @Test
    void shouldDetectDuplicatePayments() {
        BankAccountEntity account =
                BankAccountEntity.builder().id(1L).balance(BigDecimal.ZERO).build();
        List<BankTransactionEntity> transactions =
                List.of(tx(account, 100), tx(account, 100), tx(account, 100), tx(account, 100), tx(account, 100));

        when(repository.findByAccountId(1L)).thenReturn(transactions);

        BankTransactionEntity current = tx(account, 100);

        FraudAlertEntity result = processor.process(current);
        assertThat(result).isNotNull();
        assertThat(result.getFraudType()).isEqualTo(FraudType.DUPLICATE_PAYMENT.name());
    }

    @Test
    void shouldDetectRapidTransactions() {
        BankAccountEntity account =
                BankAccountEntity.builder().id(1L).balance(BigDecimal.ZERO).build();
        LocalDateTime now = LocalDateTime.now();

        List<BankTransactionEntity> transactions = List.of(
                tx(account, now.minusMinutes(1), BigDecimal.valueOf(10L)),
                tx(account, now.minusMinutes(2), BigDecimal.valueOf(20L)),
                tx(account, now.minusMinutes(3), BigDecimal.valueOf(30L)),
                tx(account, now.minusMinutes(4), BigDecimal.valueOf(40L)),
                tx(account, now.minusMinutes(5), BigDecimal.valueOf(50L)));

        when(repository.findByAccountId(1L)).thenReturn(transactions);

        BankTransactionEntity current = tx(account, now, BigDecimal.valueOf(100L));

        FraudAlertEntity result = processor.process(current);
        assertThat(result).isNotNull();
        assertThat(result.getFraudType()).isEqualTo(FraudType.RAPID_TRANSACTIONS.name());
    }

    @Test
    void shouldReturnNullWhenNoFraudDetected() {
        BankAccountEntity account =
                BankAccountEntity.builder().id(1L).balance(BigDecimal.ZERO).build();
        when(repository.findByAccountId(1L)).thenReturn(List.of());

        BankTransactionEntity transaction = tx(account, 100);

        FraudAlertEntity result = processor.process(transaction);
        assertThat(result).isNull();
    }

    private BankTransactionEntity tx(BankAccountEntity account, double amount) {
        return BankTransactionEntity.builder()
                .id(1L)
                .account(account)
                .amount(BigDecimal.valueOf(amount))
                .transactionDate(LocalDateTime.now())
                .build();
    }

    private BankTransactionEntity tx(BankAccountEntity account, LocalDateTime transactionDate, BigDecimal amount) {
        return BankTransactionEntity.builder()
                .id(1L)
                .account(account)
                .amount(amount)
                .transactionDate(transactionDate)
                .build();
    }
}
