package com.qbe.batchstarter.service;

import com.qbe.batchstarter.dto.CountryReference;
import com.qbe.batchstarter.dto.MerchantReference;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Getter
public class ReferenceDataService {

    private final List<CountryReference> countries = new ArrayList<>();
    private final List<MerchantReference> merchants = new ArrayList<>();

    private final Map<String, List<String>> firstNamesByCountry = new HashMap<>();
    private final Map<String, List<String>> lastNamesByCountry = new HashMap<>();

    @PostConstruct
    public void init() {
        loadCountries();
        loadMerchants();

        loadFirstNames("FR");
        loadFirstNames("DE");
        loadFirstNames("US");
        loadFirstNames("UK");
        loadFirstNames("ES");
        loadFirstNames("IT");

        loadLastNames("FR");
        loadLastNames("DE");
        loadLastNames("US");
        loadLastNames("UK");
        loadLastNames("ES");
        loadLastNames("IT");
    }

    public List<String> getFirstNames(String country) {
        return firstNamesByCountry.getOrDefault(country, List.of());
    }

    public List<String> getLastNames(String country) {
        return lastNamesByCountry.getOrDefault(country, List.of());
    }

    private void loadCountries() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("reference-data/countries.csv").getInputStream(), StandardCharsets.UTF_8))) {

            reader.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .forEach(values -> countries.add(new CountryReference(
                            values[0].trim(), Integer.parseInt(values[1].trim()), values[2].trim())));

        } catch (Exception e) {
            throw new IllegalStateException("Unable to load countries.csv", e);
        }
    }

    private void loadMerchants() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("reference-data/merchants.csv").getInputStream(), StandardCharsets.UTF_8))) {

            reader.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .forEach(values ->
                            merchants.add(new MerchantReference(values[0].trim(), values[1].trim(), values[2].trim())));

        } catch (Exception e) {
            throw new IllegalStateException("Unable to load merchants.csv", e);
        }
    }

    private void loadFirstNames(String country) {
        String filename = String.format("reference-data/firstnames/firstnames_%s.csv", country.toLowerCase());
        firstNamesByCountry.put(country, loadSingleColumnFile(filename));
    }

    private void loadLastNames(String country) {
        String filename = String.format("reference-data/lastnames/lastnames_%s.csv", country.toLowerCase());
        lastNamesByCountry.put(country, loadSingleColumnFile(filename));
    }

    private List<String> loadSingleColumnFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(fileName).getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines()
                    .skip(1)
                    .map(String::trim)
                    .filter(line -> !line.isBlank())
                    .toList();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load file: " + fileName, e);
        }
    }
}
