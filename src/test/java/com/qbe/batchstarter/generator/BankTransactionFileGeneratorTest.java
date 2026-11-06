package com.qbe.batchstarter.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.dto.*;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import com.qbe.batchstarter.service.RandomReferenceService;
import com.qbe.batchstarter.service.TransactionGeneratorService;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class BankTransactionFileGeneratorTest {

    private final SecureRandom random = mock(SecureRandom.class);

    @Test
    void shouldGenerateTransactions() {
        TransactionGeneratorService transactionService = mock(TransactionGeneratorService.class);
        RandomReferenceService referenceService = mock(RandomReferenceService.class);
        BankAccountDto account = new BankAccountDto(
                1L, 1L, "FR123", AccountType.CHECKING, "EUR", BigDecimal.TEN, AccountStatus.ACTIVE, LocalDate.now());
        MerchantReference merchant = new MerchantReference("Amazon", "SHOPPING", "FR");

        when(referenceService.randomMerchant()).thenReturn(merchant);
        when(transactionService.generate(anyLong(), eq(account), eq(merchant)))
                .thenAnswer(invocation -> new BankTransactionDto(
                        invocation.getArgument(0),
                        account.iban(),
                        LocalDateTime.now(),
                        TransactionType.TRANSFER,
                        BigDecimal.ONE,
                        merchant.merchantName(),
                        "EUR",
                        TransactionStatus.COMPLETED));

        BankTransactionFileGenerator generator =
                new BankTransactionFileGenerator(transactionService, referenceService, random);
        List<BankTransactionDto> transactions = generator.generate(List.of(account), 10);

        assertThat(transactions).hasSize(10);
        verify(referenceService, times(10)).randomMerchant();
        verify(transactionService, times(10)).generate(anyLong(), eq(account), eq(merchant));
    }

    @Test
    void shouldIgnoreAccountsWithBlankIban() {
        TransactionGeneratorService transactionService = mock(TransactionGeneratorService.class);
        RandomReferenceService referenceService = mock(RandomReferenceService.class);

        BankAccountDto invalidAccount = new BankAccountDto(
                1L, 1L, "", AccountType.CHECKING, "EUR", BigDecimal.TEN, AccountStatus.ACTIVE, LocalDate.now());
        BankTransactionFileGenerator generator =
                new BankTransactionFileGenerator(transactionService, referenceService, random);
        List<BankAccountDto> invalidAccounts = List.of(invalidAccount);

        assertThatThrownBy(() -> generator.generate(invalidAccounts, 1)).isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(transactionService);
    }

    @Test
    void shouldThrowExceptionWhenNoValidAccountExists() {
        TransactionGeneratorService transactionService = mock(TransactionGeneratorService.class);
        RandomReferenceService referenceService = mock(RandomReferenceService.class);

        BankAccountDto invalidAccount = new BankAccountDto(
                1L, 1L, "", AccountType.CHECKING, "EUR", BigDecimal.TEN, AccountStatus.ACTIVE, LocalDate.now());
        BankTransactionFileGenerator generator =
                new BankTransactionFileGenerator(transactionService, referenceService, random);
        List<BankAccountDto> invalidAccounts = List.of(invalidAccount);

        assertThatThrownBy(() -> generator.generate(invalidAccounts, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No valid accounts available to generate transactions");

        verifyNoInteractions(transactionService);
        verifyNoInteractions(referenceService);
    }
}
