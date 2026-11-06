package com.qbe.batchstarter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankAccountGeneratorServiceTest {

    @Mock
    private AccountGeneratorProperties properties;

    @Mock
    private Random random;

    private BankAccountGeneratorService service;

    @BeforeEach
    void setUp() {
        lenient().when(properties.checkingRate()).thenReturn(40);
        lenient().when(properties.savingsRate()).thenReturn(30);

        lenient().when(properties.activeRate()).thenReturn(80);
        lenient().when(properties.blockedRate()).thenReturn(15);

        lenient().when(properties.minBalance()).thenReturn(100.0);
        lenient().when(properties.maxBalance()).thenReturn(1000.0);

        lenient().when(properties.maxOpeningAgeInDays()).thenReturn(365);
        lenient().when(properties.invalidIbanAccountId()).thenReturn(-1L);

        service = new BankAccountGeneratorService(properties, random);
    }

    @Test
    void shouldGenerateAccount() {
        when(random.nextLong(1_000_000_000_000_000_000L)).thenReturn(123456789L);
        when(random.nextInt(90)).thenReturn(10);
        when(random.nextInt(100))
                .thenReturn(10) // type
                .thenReturn(10); // status

        when(random.nextDouble()).thenReturn(0.5);
        when(random.nextInt(366)).thenReturn(100);

        BankAccountDto account = service.generate(1L, 10L, "FR", "EUR");
        assertThat(account.id()).isEqualTo(1L);
        assertThat(account.customerId()).isEqualTo(10L);
        assertThat(account.currency()).isEqualTo("EUR");
        assertThat(account.accountType()).isEqualTo(AccountType.CHECKING);
        assertThat(account.accountStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(account.iban()).startsWith("FR");
    }

    @Test
    void shouldReturnCheckingAccount() {
        when(random.nextInt(100)).thenReturn(10);
        assertThat(service.randomAccountType()).isEqualTo(AccountType.CHECKING);
    }

    @Test
    void shouldReturnSavingsAccount() {
        when(random.nextInt(100)).thenReturn(50);
        assertThat(service.randomAccountType()).isEqualTo(AccountType.SAVINGS);
    }

    @Test
    void shouldReturnBusinessAccount() {
        when(random.nextInt(100)).thenReturn(95);
        assertThat(service.randomAccountType()).isEqualTo(AccountType.BUSINESS);
    }

    @Test
    void shouldReturnActiveStatus() {
        when(random.nextInt(100)).thenReturn(20);
        assertThat(service.randomStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void shouldReturnBlockedStatus() {
        when(random.nextInt(100)).thenReturn(90);
        assertThat(service.randomStatus()).isEqualTo(AccountStatus.BLOCKED);
    }

    @Test
    void shouldReturnClosedStatus() {
        when(random.nextInt(100)).thenReturn(99);
        assertThat(service.randomStatus()).isEqualTo(AccountStatus.CLOSED);
    }

    @Test
    void shouldGenerateBalance() {
        when(random.nextDouble()).thenReturn(0.5);

        BigDecimal balance = service.randomBalance();
        assertThat(balance).isEqualByComparingTo("550.00");
    }

    @Test
    void shouldGenerateOpeningDate() {
        when(random.nextInt(366)).thenReturn(30);

        LocalDate date = service.randomOpeningDate();
        assertThat(date).isEqualTo(LocalDate.now().minusDays(30));
    }

    @Test
    void shouldGenerateEmptyIbanForConfiguredAccount() {
        when(properties.invalidIbanAccountId()).thenReturn(999L);

        String iban = service.generateIban(999L, "FR");
        assertThat(iban).isEmpty();
    }

    @Test
    void shouldGenerateIban() {
        when(random.nextLong(1_000_000_000_000_000_000L)).thenReturn(123456789L);
        when(random.nextInt(90)).thenReturn(10);

        String iban = service.generateIban(1L, "FR");
        assertThat(iban).startsWith("FR").isNotEmpty();
    }
}
