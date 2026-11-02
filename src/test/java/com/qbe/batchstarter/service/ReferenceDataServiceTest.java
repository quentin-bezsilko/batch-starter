package com.qbe.batchstarter.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReferenceDataServiceTest {

    @Test
    void shouldLoadReferenceData() {
        ReferenceDataService service = new ReferenceDataService();

        service.init();

        assertThat(service.getCountries()).isNotEmpty();
        assertThat(service.getMerchants()).isNotEmpty();
        assertThat(service.getFirstNames("FR")).isNotEmpty();
        assertThat(service.getLastNames("FR")).isNotEmpty();
    }
}
