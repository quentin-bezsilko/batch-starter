package com.qbe.batchstarter.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MonitoringAspectTest {

    private SimpleMeterRegistry meterRegistry;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Monitored monitored;

    private MonitoringAspect aspect;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        aspect = new MonitoringAspect(meterRegistry);

        when(monitored.name()).thenReturn("batch.test");
    }

    @Test
    void shouldIncrementSuccessCounterWhenMethodSucceeds() throws Throwable {
        when(joinPoint.proceed()).thenReturn("success");

        Object result = aspect.monitor(joinPoint, monitored);
        assertThat(result).isEqualTo("success");

        assertThat(meterRegistry.get("batch.test.success").counter().count()).isEqualTo(1.0);
        assertThat(meterRegistry.get("batch.test.duration").timer().count()).isEqualTo(1);
        assertThat(meterRegistry.find("batch.test.error").counter()).isNull();
    }

    @Test
    void shouldIncrementErrorCounterWhenMethodThrowsException() throws Throwable {
        RuntimeException exception = new RuntimeException("Batch error");
        when(joinPoint.proceed()).thenThrow(exception);

        assertThatThrownBy(() -> aspect.monitor(joinPoint, monitored)).isSameAs(exception);

        assertThat(meterRegistry.get("batch.test.error").counter().count()).isEqualTo(1.0);
        assertThat(meterRegistry.get("batch.test.duration").timer().count()).isEqualTo(1);
        assertThat(meterRegistry.find("batch.test.success").counter()).isNull();
    }

    @Test
    void shouldReturnNullWhenMethodReturnsNull() throws Throwable {
        when(joinPoint.proceed()).thenReturn(null);

        Object result = aspect.monitor(joinPoint, monitored);
        assertThat(result).isNull();
        assertThat(meterRegistry.get("batch.test.success").counter().count()).isEqualTo(1.0);
    }

    @Test
    void shouldNotIncrementErrorCounterWhenMethodSucceeds() throws Throwable {
        when(joinPoint.proceed()).thenReturn("OK");
        aspect.monitor(joinPoint, monitored);

        assertThat(meterRegistry.get("batch.test.success").counter().count()).isEqualTo(1.0);
        assertThat(meterRegistry.find("batch.test.error").counter()).isNull();
    }
}
