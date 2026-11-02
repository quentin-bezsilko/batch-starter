package com.qbe.batchstarter.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class BankTransactionMapperTest {

    private final BankTransactionMapper mapper = Mappers.getMapper(BankTransactionMapper.class);

    @Test
    void shouldMapBankTransactionDtoToEntity() {
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

        BankTransactionEntity entity = mapper.toEntity(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getTransactionDate()).isEqualTo(dto.transactionDate());
        assertThat(entity.getTransactionType()).isEqualTo(dto.transactionType());
        assertThat(entity.getAmount()).isEqualByComparingTo(dto.amount());

        assertThat(entity.getId()).isNull();
        assertThat(entity.getAccount()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getVersion()).isNull();
    }
}
