package com.qbe.batchstarter.service;

import com.qbe.batchstarter.dto.CountryReference;
import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.properties.CustomerGeneratorProperties;
import java.time.LocalDate;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerGeneratorService {

    private final RandomReferenceService referenceService;
    private final CustomerGeneratorProperties properties;

    private final Random random = new Random();

    public CustomerDto generate(long id) {
        CountryReference country = referenceService.randomCountry();
        return new CustomerDto(
                id,
                referenceService.randomFirstName(country.country()),
                referenceService.randomLastName(country.country()),
                randomBirthDate(),
                country.country());
    }

    private LocalDate randomBirthDate() {
        int minAge = properties.minAge();
        int maxAge = properties.maxAge();
        int age = minAge + random.nextInt(maxAge - minAge + 1);
        return LocalDate.now().minusYears(age).minusDays(random.nextInt(365));
    }
}
