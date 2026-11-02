package com.qbe.batchstarter.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CountryReferenceTest {

    @Test
    void shouldCreateRecord() {
        CountryReference dto = new CountryReference("France", 25, "EUR");

        assertThat(dto.country()).isEqualTo("France");
        assertThat(dto.distribution()).isEqualTo(25);
        assertThat(dto.currency()).isEqualTo("EUR");
    }

    @Test
    void shouldImplementEqualsHashCodeAndToString() {
        CountryReference dto1 = new CountryReference("France", 25, "EUR");
        CountryReference dto2 = new CountryReference("France", 25, "EUR");

        assertThat(dto1).isEqualTo(dto2).hasSameHashCodeAs(dto2);
        assertThat(dto1.toString()).contains("France").contains("EUR");
    }
}
