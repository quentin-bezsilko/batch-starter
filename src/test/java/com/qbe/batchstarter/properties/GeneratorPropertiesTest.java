package com.qbe.batchstarter.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GeneratorPropertiesTest {

    @Test
    void shouldCreateProperties() {
        GeneratorProperties properties =
                new GeneratorProperties(true, "/tmp/", "customers.csv", "accounts.csv", "transactions.csv");

        assertThat(properties.simulateRetry()).isTrue();
        assertThat(properties.outputDirectory()).isEqualTo("/tmp/");
        assertThat(properties.customerFileName()).isEqualTo("customers.csv");
        assertThat(properties.accountFileName()).isEqualTo("accounts.csv");
        assertThat(properties.transactionFileName()).isEqualTo("transactions.csv");
    }
}
