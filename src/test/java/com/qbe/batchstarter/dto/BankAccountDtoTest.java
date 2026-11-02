package com.qbe.batchstarter.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BankAccountDtoTest {

    @Test
    void shouldCreateRecordAndExposeFields() {
        BankAccountDto dto = new BankAccountDto(
                1L,
                10L,
                "FR761234567890",
                AccountType.CHECKING,
                "EUR",
                BigDecimal.valueOf(1500.50),
                AccountStatus.ACTIVE,
                LocalDate.of(2020, 1, 1));

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.customerId()).isEqualTo(10L);
        assertThat(dto.iban()).isEqualTo("FR761234567890");
        assertThat(dto.accountType()).isEqualTo(AccountType.CHECKING);
        assertThat(dto.currency()).isEqualTo("EUR");
        assertThat(dto.balance()).isEqualByComparingTo("1500.50");
        assertThat(dto.accountStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(dto.openingDate()).isEqualTo(LocalDate.of(2020, 1, 1));
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        BankAccountDto dto1 = new BankAccountDto(
                1L,
                10L,
                "IBAN1",
                AccountType.CHECKING,
                "EUR",
                BigDecimal.TEN,
                AccountStatus.ACTIVE,
                LocalDate.of(2020, 1, 1));

        BankAccountDto dto2 = new BankAccountDto(
                1L,
                10L,
                "IBAN1",
                AccountType.CHECKING,
                "EUR",
                BigDecimal.TEN,
                AccountStatus.ACTIVE,
                LocalDate.of(2020, 1, 1));

        assertThat(dto1).isEqualTo(dto2).hasSameHashCodeAs(dto2);
    }
}
