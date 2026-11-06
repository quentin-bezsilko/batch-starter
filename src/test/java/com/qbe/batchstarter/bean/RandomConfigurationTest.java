package com.qbe.batchstarter.bean;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.SecureRandom;
import org.junit.jupiter.api.Test;

class RandomConfigurationTest {

    private final RandomConfiguration configuration = new RandomConfiguration();

    @Test
    void shouldCreateSecureRandom() {
        SecureRandom random = configuration.random();
        assertThat(random).isNotNull();
    }
}
