package com.qbe.batchstarter.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MerchantReferenceTest {

    @Test
    void shouldCreateRecord() {
        MerchantReference dto = new MerchantReference("Amazon", "E_COMMERCE", "France");

        assertThat(dto.merchantName()).isEqualTo("Amazon");
        assertThat(dto.category()).isEqualTo("E_COMMERCE");
        assertThat(dto.country()).isEqualTo("France");
    }

    @Test
    void shouldImplementEqualsHashCodeAndToString() {
        MerchantReference dto1 = new MerchantReference("Amazon", "E_COMMERCE", "France");
        MerchantReference dto2 = new MerchantReference("Amazon", "E_COMMERCE", "France");

        assertThat(dto1).isEqualTo(dto2).hasSameHashCodeAs(dto2);
        assertThat(dto1.toString()).contains("Amazon").contains("E_COMMERCE").contains("France");
    }
}
