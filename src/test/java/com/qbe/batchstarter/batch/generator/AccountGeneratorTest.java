package com.qbe.batchstarter.batch.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.listener.ObservedStepListener;
import com.qbe.batchstarter.tasklet.AccountGenerationTasklet;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.transaction.PlatformTransactionManager;

class AccountGeneratorTest {

    private final AccountGenerator configuration = new AccountGenerator();

    @Test
    void shouldCreateGenerateAccountsStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        AccountGenerationTasklet tasklet = mock(AccountGenerationTasklet.class);
        ObservedStepListener observedStepListener = mock(ObservedStepListener.class);

        Step step =
                configuration.generateAccountsStep(jobRepository, transactionManager, tasklet, observedStepListener);
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("generateAccountsStep");
    }
}
