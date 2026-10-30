package com.qbe.batchstarter.partition;

import com.qbe.batchstarter.properties.TransactionGeneratorProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionPartitioner implements Partitioner {

    private final TransactionGeneratorProperties transactionGeneratorProperties;

    @Override
    @NullMarked
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();
        int totalLines = transactionGeneratorProperties.count();
        int range = totalLines / gridSize;
        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();
            int startLine = i * range + 1;
            int endLine = i == gridSize - 1 ? totalLines : (i + 1) * range;
            context.putInt("startLine", startLine);
            context.putInt("endLine", endLine);
            log.info("Partition {} => startLine={}, endLine={}", i, startLine, endLine);
            partitions.put("partition" + i, context);
        }
        return partitions;
    }
}
