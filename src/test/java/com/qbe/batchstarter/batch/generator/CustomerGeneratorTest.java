package com.qbe.batchstarter.batch.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.listener.ObservedStepListener;
import com.qbe.batchstarter.tasklet.CustomerGenerationTasklet;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.transaction.PlatformTransactionManager;

class CustomerGeneratorTest {

    private final CustomerGenerator configuration = new CustomerGenerator();

    @Test
    void shouldCreateGenerateCustomersStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        CustomerGenerationTasklet tasklet = mock(CustomerGenerationTasklet.class);
        ObservedStepListener listener = mock(ObservedStepListener.class);

        Step step = configuration.generateCustomersStep(jobRepository, transactionManager, tasklet, listener);

        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("generateCustomersStep");
    }
}
