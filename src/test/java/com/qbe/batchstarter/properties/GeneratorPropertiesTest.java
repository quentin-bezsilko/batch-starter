package com.qbe.batchstarter.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GeneratorPropertiesTest {

    @Test
    void shouldCreateProperties() {
        GeneratorProperties properties = new GeneratorProperties(
                true,
                "/tmp/",
                "customers.csv",
                "accounts.csv",
                "transactions.csv",
                "customer_enrichment.csv",
                "global-report.csv",
                "customer-report.csv",
                "best-client-report.csv",
                "account-report.csv",
                "transaction-report.csv",
                "fraud-report.csv");

        assertThat(properties.simulateRetry()).isTrue();
        assertThat(properties.outputDirectory()).isEqualTo("/tmp/");
        assertThat(properties.customerFileName()).isEqualTo("customers.csv");
        assertThat(properties.accountFileName()).isEqualTo("accounts.csv");
        assertThat(properties.transactionFileName()).isEqualTo("transactions.csv");
        assertThat(properties.customerEnrichmentFileName()).isEqualTo("customer_enrichment.csv");
        assertThat(properties.globalReportFileName()).isEqualTo("global-report.csv");
        assertThat(properties.customerReportFileName()).isEqualTo("customer-report.csv");
        assertThat(properties.bestClientReportFileName()).isEqualTo("best-client-report.csv");
        assertThat(properties.accountReportFileName()).isEqualTo("account-report.csv");
        assertThat(properties.transactionReportFileName()).isEqualTo("transaction-report.csv");
        assertThat(properties.fraudReportFileName()).isEqualTo("fraud-report.csv");
    }
}
