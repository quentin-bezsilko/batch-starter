package com.qbe.batchstarter.service;

import com.qbe.batchstarter.dto.CountryReference;
import com.qbe.batchstarter.dto.MerchantReference;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomReferenceService {

    private final ReferenceDataService referenceDataService;
    private final Random random = new Random();

    public CountryReference randomCountry() {
        List<CountryReference> countries = referenceDataService.getCountries();
        int total = countries.stream().mapToInt(CountryReference::distribution).sum();
        int value = random.nextInt(total);
        int current = 0;
        for (CountryReference country : countries) {
            current += country.distribution();
            if (value < current) {
                return country;
            }
        }
        return countries.getFirst();
    }

    public String randomFirstName(String country) {
        List<String> names = referenceDataService.getFirstNames(country);
        return names.get(random.nextInt(names.size()));
    }

    public String randomLastName(String country) {
        List<String> names = referenceDataService.getLastNames(country);
        return names.get(random.nextInt(names.size()));
    }

    public MerchantReference randomMerchant() {
        List<MerchantReference> merchants = referenceDataService.getMerchants();
        return merchants.get(random.nextInt(merchants.size()));
    }
}
