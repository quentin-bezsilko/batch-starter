package com.qbe.batchstarter.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CustomerDtoTest {

    @Test
    void shouldCreateRecord() {
        CustomerDto dto = new CustomerDto(1L, "John", "Doe", LocalDate.of(1990, 5, 15), "France");

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.firstName()).isEqualTo("John");
        assertThat(dto.lastName()).isEqualTo("Doe");
        assertThat(dto.birthDate()).isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(dto.country()).isEqualTo("France");
    }

    @Test
    void shouldImplementEqualsHashCodeAndToString() {
        CustomerDto dto1 = new CustomerDto(1L, "John", "Doe", LocalDate.of(1990, 5, 15), "France");
        CustomerDto dto2 = new CustomerDto(1L, "John", "Doe", LocalDate.of(1990, 5, 15), "France");

        assertThat(dto1).isEqualTo(dto2).hasSameHashCodeAs(dto2);
        assertThat(dto1.toString()).contains("John").contains("Doe").contains("France");
    }
}
