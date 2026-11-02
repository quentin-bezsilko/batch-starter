package com.qbe.batchstarter.partitioner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.partition.TransactionPartitioner;
import com.qbe.batchstarter.properties.TransactionGeneratorProperties;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.infrastructure.item.ExecutionContext;

class TransactionPartitionerTest {

    @Test
    void shouldCreatePartitions() {
        TransactionGeneratorProperties properties = Mockito.mock(TransactionGeneratorProperties.class);
        when(properties.count()).thenReturn(1_000_000);

        TransactionPartitioner partitioner = new TransactionPartitioner(properties);

        Map<String, ExecutionContext> partitions = partitioner.partition(4);
        assertThat(partitions).hasSize(4);

        assertThat(partitions.get("partition0").getInt("startLine")).isEqualTo(1);
        assertThat(partitions.get("partition0").getInt("endLine")).isEqualTo(250_000);

        assertThat(partitions.get("partition1").getInt("startLine")).isEqualTo(250_001);
        assertThat(partitions.get("partition1").getInt("endLine")).isEqualTo(500_000);

        assertThat(partitions.get("partition2").getInt("startLine")).isEqualTo(500_001);
        assertThat(partitions.get("partition2").getInt("endLine")).isEqualTo(750_000);

        assertThat(partitions.get("partition3").getInt("startLine")).isEqualTo(750_001);
        assertThat(partitions.get("partition3").getInt("endLine")).isEqualTo(1_000_000);
    }

    @Test
    void shouldAssignRemainingLinesToLastPartition() {
        TransactionGeneratorProperties properties = Mockito.mock(TransactionGeneratorProperties.class);
        when(properties.count()).thenReturn(10);

        TransactionPartitioner partitioner = new TransactionPartitioner(properties);

        Map<String, ExecutionContext> partitions = partitioner.partition(3);

        assertThat(partitions).hasSize(3);
        assertThat(partitions.get("partition0").getInt("startLine")).isEqualTo(1);
        assertThat(partitions.get("partition0").getInt("endLine")).isEqualTo(3);

        assertThat(partitions.get("partition1").getInt("startLine")).isEqualTo(4);
        assertThat(partitions.get("partition1").getInt("endLine")).isEqualTo(6);

        assertThat(partitions.get("partition2").getInt("startLine")).isEqualTo(7);
        assertThat(partitions.get("partition2").getInt("endLine")).isEqualTo(10);
    }
}
