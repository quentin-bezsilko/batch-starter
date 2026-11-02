package com.qbe.batchstarter.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import com.qbe.batchstarter.service.BankAccountGeneratorService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class BankAccountFileGeneratorTest {

    @Test
    void shouldGenerateAccountsForCustomers() {
        BankAccountGeneratorService service = mock(BankAccountGeneratorService.class);
        AccountGeneratorProperties properties = mock(AccountGeneratorProperties.class);

        when(properties.singleAccountRate()).thenReturn(100);
        when(properties.doubleAccountRate()).thenReturn(0);

        when(service.generate(anyLong(), anyLong(), anyString(), anyString()))
                .thenAnswer(invocation -> new BankAccountDto(
                        invocation.getArgument(0),
                        invocation.getArgument(1),
                        "IBAN",
                        AccountType.CHECKING,
                        invocation.getArgument(3),
                        BigDecimal.ZERO,
                        AccountStatus.ACTIVE,
                        LocalDate.now()));

        BankAccountFileGenerator generator = new BankAccountFileGenerator(service, properties);

        List<CustomerDto> customers = List.of(
                new CustomerDto(1L, "John", "Doe", LocalDate.now(), "FR"),
                new CustomerDto(2L, "John", "Doe", LocalDate.now(), "US"),
                new CustomerDto(3L, "John", "Doe", LocalDate.now(), "UK"));

        List<BankAccountDto> accounts = generator.generate(customers);
        assertThat(accounts).hasSize(3);
        assertThat(accounts.get(0).currency()).isEqualTo("EUR");
        assertThat(accounts.get(1).currency()).isEqualTo("USD");
        assertThat(accounts.get(2).currency()).isEqualTo("GBP");
    }
}
