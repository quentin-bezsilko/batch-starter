package com.qbe.batchstarter.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class BankTransactionDtoTest {

    @Test
    void shouldCreateRecord() {
        LocalDateTime transactionDate = LocalDateTime.of(2024, 1, 1, 12, 0);

        BankTransactionDto dto = new BankTransactionDto(
                1L,
                "FR761234567890",
                transactionDate,
                TransactionType.TRANSFER,
                BigDecimal.valueOf(100),
                "Amazon",
                "EUR",
                TransactionStatus.COMPLETED);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.accountIban()).isEqualTo("FR761234567890");
        assertThat(dto.transactionDate()).isEqualTo(transactionDate);
        assertThat(dto.transactionType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(dto.amount()).isEqualByComparingTo("100");
        assertThat(dto.merchantName()).isEqualTo("Amazon");
        assertThat(dto.currency()).isEqualTo("EUR");
        assertThat(dto.transactionStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    void shouldImplementEqualsHashCodeAndToString() {
        LocalDateTime transactionDate = LocalDateTime.of(2024, 1, 1, 12, 0);

        BankTransactionDto dto1 = new BankTransactionDto(
                1L,
                "IBAN1",
                transactionDate,
                TransactionType.TRANSFER,
                BigDecimal.TEN,
                "AMAZON",
                "EUR",
                TransactionStatus.COMPLETED);

        BankTransactionDto dto2 = new BankTransactionDto(
                1L,
                "IBAN1",
                transactionDate,
                TransactionType.TRANSFER,
                BigDecimal.TEN,
                "AMAZON",
                "EUR",
                TransactionStatus.COMPLETED);

        assertThat(dto1).isEqualTo(dto2).hasSameHashCodeAs(dto2);
        assertThat(dto1.toString()).contains("IBAN1").contains("AMAZON");
    }
}
