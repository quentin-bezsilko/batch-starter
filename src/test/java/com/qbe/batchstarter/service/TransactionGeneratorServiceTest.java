package com.qbe.batchstarter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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
import org.junit.jupiter.api.Test;

public class TransactionGeneratorServiceTest {

    @Test
    void shouldGenerateTransaction() {
        TransactionGeneratorProperties properties = mock(TransactionGeneratorProperties.class);
        when(properties.cardPaymentRate()).thenReturn(100);
        when(properties.transferRate()).thenReturn(0);
        when(properties.directDebitRate()).thenReturn(0);

        TransactionGeneratorService service = new TransactionGeneratorService(properties);

        BankAccountDto account = new BankAccountDto(
                1L, 1L, "FR123", AccountType.CHECKING, "EUR", BigDecimal.TEN, AccountStatus.ACTIVE, LocalDate.now());
        MerchantReference merchant = new MerchantReference("Amazon", "SHOPPING", "FR");
        BankTransactionDto transaction = service.generate(1L, account, merchant);

        assertThat(transaction.id()).isEqualTo(1L);
        assertThat(transaction.accountIban()).isEqualTo("FR123");
        assertThat(transaction.currency()).isEqualTo("EUR");
        assertThat(transaction.merchantName()).isEqualTo("Amazon");
        assertThat(transaction.transactionType()).isEqualTo(TransactionType.CARD_PAYMENT);
        assertThat(transaction.amount()).isBetween(BigDecimal.valueOf(20), BigDecimal.valueOf(1000));
        assertThat(transaction.transactionDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(transaction.transactionStatus())
                .isIn(TransactionStatus.COMPLETED, TransactionStatus.PENDING, TransactionStatus.REJECTED);
    }
}
