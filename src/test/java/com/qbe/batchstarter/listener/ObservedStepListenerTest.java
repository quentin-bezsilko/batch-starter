package com.qbe.batchstarter.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.ExecutionContext;

@ExtendWith(MockitoExtension.class)
class ObservedStepListenerTest {

    @Mock
    private ObservationRegistry registry;

    @Mock
    private Observation observation;

    @InjectMocks
    private ObservedStepListener listener;

    @Test
    void shouldStartObservationBeforeStep() {
        StepExecution stepExecution = mock(StepExecution.class);
        ExecutionContext executionContext = new ExecutionContext();

        when(stepExecution.getExecutionContext()).thenReturn(executionContext);
        when(stepExecution.getStepName()).thenReturn("transactionReportStep");

        JobExecution jobExecution = mock(JobExecution.class);
        JobInstance jobInstance = mock(JobInstance.class);

        when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        when(jobExecution.getJobInstance()).thenReturn(jobInstance);
        when(jobInstance.getJobName()).thenReturn("transactionReportJob");

        try (MockedStatic<Observation> mockedObservation = Mockito.mockStatic(Observation.class)) {
            mockedObservation
                    .when(() -> Observation.start("spring.batch.step", registry))
                    .thenReturn(observation);

            listener.beforeStep(stepExecution);

            verify(observation).lowCardinalityKeyValue("step", "transactionReportStep");
            verify(observation).lowCardinalityKeyValue("job", "transactionReportJob");

            assertThat(executionContext.get("observation")).isSameAs(observation);
        }
    }

    @Test
    void shouldStopObservationAfterStep() {
        StepExecution stepExecution = mock(StepExecution.class);

        ExecutionContext executionContext = new ExecutionContext();
        executionContext.put("observation", observation);

        when(stepExecution.getExecutionContext()).thenReturn(executionContext);
        when(stepExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);
        when(stepExecution.getExitStatus()).thenReturn(ExitStatus.COMPLETED);

        listener.afterStep(stepExecution);
        verify(observation).stop();
    }
}
