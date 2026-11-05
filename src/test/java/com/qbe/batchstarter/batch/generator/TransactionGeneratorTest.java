package com.qbe.batchstarter.batch.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.listener.ObservedStepListener;
import com.qbe.batchstarter.tasklet.TransactionGenerationTasklet;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.transaction.PlatformTransactionManager;

class TransactionGeneratorTest {

    private final TransactionGenerator configuration = new TransactionGenerator();

    @Test
    void shouldCreateGenerateTransactionsStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        TransactionGenerationTasklet tasklet = mock(TransactionGenerationTasklet.class);
        ObservedStepListener listener = mock(ObservedStepListener.class);

        Step step = configuration.generateTransactionsStep(jobRepository, transactionManager, tasklet, listener);
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("generateTransactionsStep");
    }
}
