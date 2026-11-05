package com.qbe.batchstarter.listener;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObservedStepListener implements StepExecutionListener {

    private static final String OBSERVATION_KEY = "observation";

    private final ObservationRegistry registry;

    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        Observation observation = Observation.start("spring.batch.step", registry);
        observation.lowCardinalityKeyValue("step", stepExecution.getStepName());
        observation.lowCardinalityKeyValue(
                "job", stepExecution.getJobExecution().getJobInstance().getJobName());
        stepExecution.getExecutionContext().put(OBSERVATION_KEY, observation);
    }

    @Override
    public @NonNull ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        Object value = stepExecution.getExecutionContext().get(OBSERVATION_KEY);
        if (value instanceof Observation observation) {
            observation.lowCardinalityKeyValue(
                    "status", stepExecution.getStatus().name());
            observation.lowCardinalityKeyValue("readCount", String.valueOf(stepExecution.getReadCount()));
            observation.lowCardinalityKeyValue("writeCount", String.valueOf(stepExecution.getWriteCount()));
            observation.lowCardinalityKeyValue("skipCount", String.valueOf(stepExecution.getSkipCount()));
            observation.stop();
        }
        return stepExecution.getExitStatus();
    }
}
