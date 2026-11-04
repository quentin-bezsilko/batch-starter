package com.qbe.batchstarter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.dto.MerchantReference;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import com.qbe.batchstarter.properties.TransactionGeneratorProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionGeneratorServiceTest {

    @Mock
    private TransactionGeneratorProperties properties;

    @Mock
    private Random random;

    private TransactionGeneratorService service;

    @BeforeEach
    void setUp() {

        lenient().when(properties.cardPaymentRate()).thenReturn(40);
        lenient().when(properties.transferRate()).thenReturn(30);
        lenient().when(properties.directDebitRate()).thenReturn(20);

        service = new TransactionGeneratorService(properties, random);
    }

    @Test
    void shouldGenerateTransaction() {
        BankAccountDto account = new BankAccountDto(
                1L, 1L, "FR123", AccountType.BUSINESS, "EUR", BigDecimal.TEN, AccountStatus.ACTIVE, LocalDate.now());

        MerchantReference merchant = new MerchantReference("Amazon", "SHOPPING", "US");
        when(random.nextInt(365)).thenReturn(1);
        when(random.nextInt(24)).thenReturn(1);
        when(random.nextInt(60)).thenReturn(1);
        when(random.nextInt(100)).thenReturn(10).thenReturn(10);
        when(random.nextDouble()).thenReturn(0.5).thenReturn(0.5);

        BankTransactionDto result = service.generate(1L, account, merchant);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.accountIban()).isEqualTo("FR123");
        assertThat(result.currency()).isEqualTo("EUR");
    }

    @Test
    void shouldReturnCardPayment() {
        when(random.nextInt(100)).thenReturn(10);
        assertThat(service.randomTransactionType()).isEqualTo(TransactionType.CARD_PAYMENT);
    }

    @Test
    void shouldReturnTransfer() {
        when(random.nextInt(100)).thenReturn(50);
        assertThat(service.randomTransactionType()).isEqualTo(TransactionType.TRANSFER);
    }

    @Test
    void shouldReturnDirectDebit() {
        when(random.nextInt(100)).thenReturn(80);
        assertThat(service.randomTransactionType()).isEqualTo(TransactionType.DIRECT_DEBIT);
    }

    @Test
    void shouldReturnCashWithdrawal() {
        when(random.nextInt(100)).thenReturn(95);
        assertThat(service.randomTransactionType()).isEqualTo(TransactionType.CASH_WITHDRAWAL);
    }

    @Test
    void shouldReturnCompletedStatus() {
        when(random.nextInt(100)).thenReturn(50);
        assertThat(service.randomStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    void shouldReturnPendingStatus() {
        when(random.nextInt(100)).thenReturn(97);
        assertThat(service.randomStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    void shouldReturnRejectedStatus() {
        when(random.nextInt(100)).thenReturn(99);
        assertThat(service.randomStatus()).isEqualTo(TransactionStatus.REJECTED);
    }

    @Test
    void shouldGenerateFoodAmount() {
        when(random.nextDouble()).thenReturn(0.5).thenReturn(0.5);

        BigDecimal result = service.randomAmount("FOOD");
        assertThat(result).isBetween(BigDecimal.valueOf(5), BigDecimal.valueOf(120));
    }

    @Test
    void shouldGenerateTransportAmount() {
        when(random.nextDouble()).thenReturn(0.5).thenReturn(0.5);

        BigDecimal result = service.randomAmount("TRANSPORT");
        assertThat(result).isBetween(BigDecimal.valueOf(2), BigDecimal.valueOf(50));
    }

    @Test
    void shouldGenerateShoppingAmount() {
        when(random.nextDouble()).thenReturn(0.5).thenReturn(0.5);

        BigDecimal result = service.randomAmount("SHOPPING");
        assertThat(result).isBetween(BigDecimal.valueOf(20), BigDecimal.valueOf(1000));
    }

    @Test
    void shouldGenerateUtilitiesAmount() {
        when(random.nextDouble()).thenReturn(0.5).thenReturn(0.5);

        BigDecimal result = service.randomAmount("UTILITIES");
        assertThat(result).isBetween(BigDecimal.valueOf(20), BigDecimal.valueOf(500));
    }

    @Test
    void shouldGenerateEntertainmentAmount() {
        when(random.nextDouble()).thenReturn(0.5).thenReturn(0.5);

        BigDecimal result = service.randomAmount("ENTERTAINMENT");
        assertThat(result).isBetween(BigDecimal.valueOf(5), BigDecimal.valueOf(100));
    }

    @Test
    void shouldGenerateDefaultAmount() {
        when(random.nextDouble()).thenReturn(0.5).thenReturn(0.5);

        BigDecimal result = service.randomAmount("UNKNOWN");
        assertThat(result).isBetween(BigDecimal.valueOf(5), BigDecimal.valueOf(300));
    }

    @Test
    void shouldGeneratePastDate() {
        when(random.nextInt(365)).thenReturn(1);
        when(random.nextInt(24)).thenReturn(1);
        when(random.nextInt(60)).thenReturn(1);

        LocalDateTime result = service.randomTransactionDate();
        assertThat(result).isBefore(LocalDateTime.now());
    }

    @Test
    void shouldGenerateAmountBetweenBounds() {
        when(random.nextDouble()).thenReturn(0.5);

        BigDecimal result = service.amountBetween(10, 20);
        assertThat(result).isEqualByComparingTo("15.00");
    }

    @Test
    void shouldGenerateHighFraudAmount() {
        when(random.nextDouble())
                .thenReturn(0.5) // amountBetween(5,120)
                .thenReturn(0.001) // déclenche la fraude
                .thenReturn(0.5); // amountBetween(10000,50000)

        BigDecimal result = service.randomAmount("FOOD");
        assertThat(result)
                .isGreaterThanOrEqualTo(BigDecimal.valueOf(10_000))
                .isLessThanOrEqualTo(BigDecimal.valueOf(50_000));
    }
}
