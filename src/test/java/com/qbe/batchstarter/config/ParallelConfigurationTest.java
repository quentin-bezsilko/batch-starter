package com.qbe.batchstarter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class ParallelConfigurationTest {

    @Test
    void shouldCreateTaskExecutor() {
        BatchProperties properties = Mockito.mock(BatchProperties.class);
        when(properties.parallelThreads()).thenReturn(8);

        ParallelConfiguration configuration = new ParallelConfiguration(properties);

        AsyncTaskExecutor executor = configuration.taskExecutor();
        assertThat(executor).isInstanceOf(ThreadPoolTaskExecutor.class);

        ThreadPoolTaskExecutor pool = (ThreadPoolTaskExecutor) executor;
        assertThat(pool.getCorePoolSize()).isEqualTo(8);
        assertThat(pool.getMaxPoolSize()).isEqualTo(8);
        assertThat(pool.getThreadNamePrefix()).isEqualTo("batch-");
    }
}
