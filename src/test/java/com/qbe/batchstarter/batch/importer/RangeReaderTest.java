package com.qbe.batchstarter.batch.importer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamReader;

@ExtendWith(MockitoExtension.class)
class RangeReaderTest {

    @Mock
    private ItemStreamReader<String> delegate;

    private RangeReader<String> reader;

    @Test
    void shouldReadItemsWithinMaxItems() throws Exception {
        reader = new RangeReader<>(delegate, 3);

        when(delegate.read()).thenReturn("item1", "item2", "item3", "item4");

        assertThat(reader.read()).isEqualTo("item1");
        assertThat(reader.read()).isEqualTo("item2");
        assertThat(reader.read()).isEqualTo("item3");

        assertThat(reader.read()).isNull();

        verify(delegate, times(3)).read();
    }

    @Test
    void shouldReturnNullWhenMaxItemsIsZero() throws Exception {
        reader = new RangeReader<>(delegate, 0);
        assertThat(reader.read()).isNull();
        verify(delegate, never()).read();
    }

    @Test
    void shouldReturnNullWhenDelegateHasNoMoreItems() throws Exception {
        reader = new RangeReader<>(delegate, 5);
        when(delegate.read()).thenReturn("item1", "item2", null);
        assertThat(reader.read()).isEqualTo("item1");
        assertThat(reader.read()).isEqualTo("item2");
        assertThat(reader.read()).isNull();
        verify(delegate, times(3)).read();
    }

    @Test
    void shouldNotIncrementCountWhenDelegateReturnsNull() throws Exception {
        reader = new RangeReader<>(delegate, 2);
        when(delegate.read()).thenReturn(null, "item1", "item2");
        assertThat(reader.read()).isNull();
        assertThat(reader.read()).isEqualTo("item1");
        assertThat(reader.read()).isEqualTo("item2");
        assertThat(reader.read()).isNull();
        verify(delegate, times(3)).read();
    }

    @Test
    void shouldDelegateOpen() {
        reader = new RangeReader<>(delegate, 10);

        ExecutionContext executionContext = new ExecutionContext();
        reader.open(executionContext);

        verify(delegate).open(executionContext);
    }

    @Test
    void shouldDelegateUpdate() {
        reader = new RangeReader<>(delegate, 10);
        ExecutionContext executionContext = new ExecutionContext();

        reader.update(executionContext);

        verify(delegate).update(executionContext);
    }

    @Test
    void shouldDelegateClose() {
        reader = new RangeReader<>(delegate, 10);
        reader.close();
        verify(delegate).close();
    }

    @Test
    void shouldPropagateExceptionFromDelegate() throws Exception {
        reader = new RangeReader<>(delegate, 10);

        RuntimeException exception = new RuntimeException("Reader error");
        when(delegate.read()).thenThrow(exception);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> reader.read()).isSameAs(exception);
    }
}
