package com.qbe.batchstarter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.CountryReference;
import com.qbe.batchstarter.dto.MerchantReference;
import java.security.SecureRandom;
import java.util.List;
import org.junit.jupiter.api.Test;

class RandomReferenceServiceTest {

    private final SecureRandom random = mock(SecureRandom.class);

    @Test
    void shouldReturnCountryFromReferenceList() {
        ReferenceDataService referenceDataService = mock(ReferenceDataService.class);

        List<CountryReference> countries =
                List.of(new CountryReference("FR", 50, "EUR"), new CountryReference("US", 50, "USD"));
        when(referenceDataService.getCountries()).thenReturn(countries);

        RandomReferenceService service = new RandomReferenceService(referenceDataService, random);

        CountryReference country = service.randomCountry();
        assertThat(countries).contains(country);
    }

    @Test
    void shouldReturnFirstName() {
        ReferenceDataService referenceDataService = mock(ReferenceDataService.class);
        when(referenceDataService.getFirstNames("FR")).thenReturn(List.of("Jean"));

        RandomReferenceService service = new RandomReferenceService(referenceDataService, random);
        assertThat(service.randomFirstName("FR")).isEqualTo("Jean");
    }

    @Test
    void shouldReturnLastName() {
        ReferenceDataService referenceDataService = mock(ReferenceDataService.class);
        when(referenceDataService.getLastNames("FR")).thenReturn(List.of("Dupont"));

        RandomReferenceService service = new RandomReferenceService(referenceDataService, random);
        assertThat(service.randomLastName("FR")).isEqualTo("Dupont");
    }

    @Test
    void shouldReturnMerchant() {
        ReferenceDataService referenceDataService = mock(ReferenceDataService.class);

        MerchantReference merchant = new MerchantReference("Amazon", "SHOPPING", "FR");
        when(referenceDataService.getMerchants()).thenReturn(List.of(merchant));

        RandomReferenceService service = new RandomReferenceService(referenceDataService, random);
        assertThat(service.randomMerchant()).isEqualTo(merchant);
    }
}
