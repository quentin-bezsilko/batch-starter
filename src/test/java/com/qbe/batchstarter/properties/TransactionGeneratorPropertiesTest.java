package com.qbe.batchstarter.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TransactionGeneratorPropertiesTest {

    @Test
    void shouldCreateProperties() {
        TransactionGeneratorProperties properties =
                new TransactionGeneratorProperties(1_000_000, 60, 20, 15, 5, 95, 4, 1, 0.002);

        assertThat(properties.count()).isEqualTo(1_000_000);
        assertThat(properties.cardPaymentRate()).isEqualTo(60);
        assertThat(properties.transferRate()).isEqualTo(20);
        assertThat(properties.directDebitRate()).isEqualTo(15);
        assertThat(properties.cashWithdrawalRate()).isEqualTo(5);
        assertThat(properties.completedRate()).isEqualTo(95);
        assertThat(properties.pendingRate()).isEqualTo(4);
        assertThat(properties.rejectedRate()).isEqualTo(1);
        assertThat(properties.highAmountFraudRate()).isEqualTo(0.002);
    }
}
