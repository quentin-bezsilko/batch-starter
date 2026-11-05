package com.qbe.batchstarter.listener;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObservedJobListener implements JobExecutionListener {

    private final ObservationRegistry registry;
    private Observation observation;

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        observation = Observation.start("spring.batch.job", registry);
        observation.lowCardinalityKeyValue("job", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        observation.stop();
    }
}
