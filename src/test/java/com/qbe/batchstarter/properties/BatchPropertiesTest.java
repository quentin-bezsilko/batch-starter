package com.qbe.batchstarter.properties;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BatchPropertiesTest {

    @Test
    void shouldCreateProperties() {
        BatchProperties properties = new BatchProperties(100, 8, 4, 50);

        assertThat(properties.chunkSize()).isEqualTo(100);
        assertThat(properties.parallelThreads()).isEqualTo(8);
        assertThat(properties.partitioningGridSize()).isEqualTo(4);
        assertThat(properties.skipLimit()).isEqualTo(50);
    }
}
