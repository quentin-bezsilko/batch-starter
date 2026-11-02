package com.qbe.batchstarter.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AccountGeneratorPropertiesTest {

    @Test
    void shouldCreateProperties() {
        AccountGeneratorProperties properties =
                new AccountGeneratorProperties(1000, 999L, 60, 30, 10, 70, 20, 10, 90, 5, 5, 0.0, 10000.0, 3650);

        assertThat(properties.crashAfter()).isEqualTo(1000);
        assertThat(properties.invalidIbanAccountId()).isEqualTo(999L);
        assertThat(properties.singleAccountRate()).isEqualTo(60);
        assertThat(properties.doubleAccountRate()).isEqualTo(30);
        assertThat(properties.tripleAccountRate()).isEqualTo(10);
        assertThat(properties.checkingRate()).isEqualTo(70);
        assertThat(properties.savingsRate()).isEqualTo(20);
        assertThat(properties.businessRate()).isEqualTo(10);
        assertThat(properties.activeRate()).isEqualTo(90);
        assertThat(properties.blockedRate()).isEqualTo(5);
        assertThat(properties.closedRate()).isEqualTo(5);
        assertThat(properties.minBalance()).isEqualTo(0.0);
        assertThat(properties.maxBalance()).isEqualTo(10000.0);
        assertThat(properties.maxOpeningAgeInDays()).isEqualTo(3650);
    }
}
