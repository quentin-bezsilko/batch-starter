package com.qbe.batchstarter.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CustomerMapperTest {

    private final CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void shouldMapCustomerDtoToEntity() {
        CustomerDto dto = new CustomerDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "FR");
        CustomerEntity entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();

        assertThat(entity.getFirstName()).isEqualTo("John");
        assertThat(entity.getLastName()).isEqualTo("Doe");
        assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(entity.getCountry()).isEqualTo("FR");

        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
    }
}
