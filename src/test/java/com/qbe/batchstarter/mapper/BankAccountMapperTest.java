package com.qbe.batchstarter.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class BankAccountMapperTest {

    private final BankAccountMapper mapper = Mappers.getMapper(BankAccountMapper.class);

    @Test
    void shouldMapBankAccountDtoToEntity() {
        BankAccountDto dto = new BankAccountDto(
                1L,
                10L,
                "FR761234567890",
                AccountType.CHECKING,
                "EUR",
                BigDecimal.valueOf(1500.50),
                AccountStatus.ACTIVE,
                LocalDate.of(2020, 1, 1));

        BankAccountEntity entity = mapper.toEntity(dto);
        assertThat(entity).isNotNull();

        assertThat(entity.getIban()).isEqualTo(dto.iban());
        assertThat(entity.getAccountType()).isEqualTo(dto.accountType());
        assertThat(entity.getCurrency()).isEqualTo(dto.currency());
        assertThat(entity.getBalance()).isEqualByComparingTo(dto.balance());
        assertThat(entity.getAccountStatus()).isEqualTo(dto.accountStatus());
        assertThat(entity.getOpeningDate()).isEqualTo(dto.openingDate());

        assertThat(entity.getId()).isNull();
        assertThat(entity.getCustomer()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getVersion()).isNull();
    }
}
