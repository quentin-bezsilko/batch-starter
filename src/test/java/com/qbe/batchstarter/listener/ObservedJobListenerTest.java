package com.qbe.batchstarter.listener;

import static org.mockito.Mockito.*;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;

@ExtendWith(MockitoExtension.class)
class ObservedJobListenerTest {

    @Mock
    private ObservationRegistry registry;

    @Mock
    private Observation observation;

    @Mock
    private JobExecution jobExecution;

    @Mock
    private JobInstance jobInstance;

    private ObservedJobListener listener;

    @BeforeEach
    void setUp() {
        listener = new ObservedJobListener(registry);
    }

    @Test
    void shouldStartObservationBeforeJob() {
        when(jobExecution.getJobInstance()).thenReturn(jobInstance);
        when(jobInstance.getJobName()).thenReturn("transactionReportJob");

        try (MockedStatic<Observation> mockedObservation = mockStatic(Observation.class)) {
            mockedObservation
                    .when(() -> Observation.start("spring.batch.job", registry))
                    .thenReturn(observation);
            listener.beforeJob(jobExecution);
            mockedObservation.verify(() -> Observation.start("spring.batch.job", registry));
            verify(observation).lowCardinalityKeyValue("job", "transactionReportJob");
        }
    }

    @Test
    void shouldStopObservationAfterJob() {
        when(jobExecution.getJobInstance()).thenReturn(jobInstance);
        when(jobInstance.getJobName()).thenReturn("transactionReportJob");

        try (MockedStatic<Observation> mockedObservation = mockStatic(Observation.class)) {
            mockedObservation
                    .when(() -> Observation.start("spring.batch.job", registry))
                    .thenReturn(observation);
            listener.beforeJob(jobExecution);
            listener.afterJob(jobExecution);
            verify(observation).stop();
        }
    }
}
