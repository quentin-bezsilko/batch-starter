package com.qbe.batchstarter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.CountryReference;
import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.properties.CustomerGeneratorProperties;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class CustomerGeneratorServiceTest {

    @Test
    void shouldGenerateCustomer() {
        RandomReferenceService referenceService = mock(RandomReferenceService.class);
        CustomerGeneratorProperties properties = mock(CustomerGeneratorProperties.class);

        when(referenceService.randomCountry()).thenReturn(new CountryReference("FR", 30, "EUR"));
        when(referenceService.randomFirstName("FR")).thenReturn("Jean");
        when(referenceService.randomLastName("FR")).thenReturn("Dupont");
        when(properties.minAge()).thenReturn(18);
        when(properties.maxAge()).thenReturn(80);

        CustomerGeneratorService service = new CustomerGeneratorService(referenceService, properties);
        CustomerDto customer = service.generate(1L);

        assertThat(customer.id()).isEqualTo(1L);
        assertThat(customer.firstName()).isEqualTo("Jean");
        assertThat(customer.lastName()).isEqualTo("Dupont");
        assertThat(customer.country()).isEqualTo("FR");

        int age = LocalDate.now().getYear() - customer.birthDate().getYear();
        assertThat(age).isBetween(18, 81);
    }
}
