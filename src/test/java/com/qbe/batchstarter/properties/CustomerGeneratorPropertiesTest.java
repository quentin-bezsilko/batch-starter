package com.qbe.batchstarter.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CustomerGeneratorPropertiesTest {

    @Test
    void shouldCreateProperties() {
        CustomerGeneratorProperties properties = new CustomerGeneratorProperties(100_000, 18, 90, 0.2, 0.8);

        assertThat(properties.count()).isEqualTo(100_000);
        assertThat(properties.minAge()).isEqualTo(18);
        assertThat(properties.maxAge()).isEqualTo(90);
        assertThat(properties.minorRate()).isEqualTo(0.2);
        assertThat(properties.majorRate()).isEqualTo(0.8);
    }
}
