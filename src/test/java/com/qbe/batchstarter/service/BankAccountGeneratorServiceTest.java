package com.qbe.batchstarter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class BankAccountGeneratorServiceTest {

    @Test
    void shouldGenerateAccount() {
        AccountGeneratorProperties properties = mock(AccountGeneratorProperties.class);
        when(properties.checkingRate()).thenReturn(100);
        when(properties.savingsRate()).thenReturn(0);
        when(properties.activeRate()).thenReturn(100);
        when(properties.blockedRate()).thenReturn(0);
        when(properties.minBalance()).thenReturn(100.0);
        when(properties.maxBalance()).thenReturn(1000.0);
        when(properties.maxOpeningAgeInDays()).thenReturn(365);
        when(properties.invalidIbanAccountId()).thenReturn(-1L);

        BankAccountGeneratorService service = new BankAccountGeneratorService(properties);
        BankAccountDto account = service.generate(1L, 10L, "FR", "EUR");

        assertThat(account.id()).isEqualTo(1L);
        assertThat(account.customerId()).isEqualTo(10L);
        assertThat(account.currency()).isEqualTo("EUR");
        assertThat(account.accountType()).isEqualTo(AccountType.CHECKING);
        assertThat(account.accountStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(account.iban()).startsWith("FR");

        assertThat(account.balance()).isBetween(BigDecimal.valueOf(100), BigDecimal.valueOf(1000));
        assertThat(account.openingDate()).isBetween(LocalDate.now().minusDays(365), LocalDate.now());
    }

    @Test
    void shouldGenerateEmptyIbanForConfiguredAccount() {
        AccountGeneratorProperties properties = mock(AccountGeneratorProperties.class);

        when(properties.checkingRate()).thenReturn(100);
        when(properties.savingsRate()).thenReturn(0);
        when(properties.activeRate()).thenReturn(100);
        when(properties.blockedRate()).thenReturn(0);
        when(properties.minBalance()).thenReturn(0.0);
        when(properties.maxBalance()).thenReturn(100.0);
        when(properties.maxOpeningAgeInDays()).thenReturn(10);
        when(properties.invalidIbanAccountId()).thenReturn(999L);

        BankAccountGeneratorService service = new BankAccountGeneratorService(properties);
        BankAccountDto account = service.generate(999L, 1L, "FR", "EUR");
        assertThat(account.iban()).isEmpty();
    }
}
