package com.qbe.batchstarter.batch.importer;

import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamReader;

public class RangeReader<T> implements ItemStreamReader<T> {

    private final ItemStreamReader<T> delegate;
    private final long maxItems;
    private long count;

    public RangeReader(ItemStreamReader<T> delegate, long maxItems) {
        this.delegate = delegate;
        this.maxItems = maxItems;
    }

    @Override
    public T read() throws Exception {
        if (count >= maxItems) {
            return null;
        }

        T item = delegate.read();

        if (item != null) {
            count++;
        }

        return item;
    }

    @Override
    public void open(@NonNull ExecutionContext executionContext) {
        delegate.open(executionContext);
    }

    @Override
    public void update(@NonNull ExecutionContext executionContext) {
        delegate.update(executionContext);
    }

    @Override
    public void close() {
        delegate.close();
    }
}
