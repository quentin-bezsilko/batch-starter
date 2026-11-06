package com.qbe.batchstarter.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BatchJobStatusDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.StepExecution;

@ExtendWith(MockitoExtension.class)
class BatchStatusControllerTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private BatchStatusController controller;

    @Test
    void shouldReturnRunningSteps() {
        JobInstance jobInstance = new JobInstance(1L, "dataWorkflowJob");

        JobExecution execution = new JobExecution(100L, jobInstance, new JobParameters());
        execution.setStatus(BatchStatus.STARTED);

        StepExecution stepExecution = mock(StepExecution.class);
        when(stepExecution.getStepName()).thenReturn("transactionWorkerStep");
        when(stepExecution.getStatus()).thenReturn(BatchStatus.STARTED);

        execution.addStepExecution(stepExecution);
        stepExecution.setStatus(BatchStatus.STARTED);

        when(jobRepository.getJobNames()).thenReturn(List.of("dataWorkflowJob"));
        when(jobRepository.getJobInstances("dataWorkflowJob", 0, Integer.MAX_VALUE))
                .thenReturn(List.of(jobInstance));
        when(jobRepository.getJobExecutions(jobInstance)).thenReturn(List.of(execution));

        List<String> result = controller.getRunningSteps();
        assertThat(result).containsExactly("transactionWorkerStep - STARTED");
    }

    @Test
    void shouldReturnEmptyRunningStepsWhenNoExecutionIsRunning() {

        JobInstance jobInstance = new JobInstance(1L, "dataWorkflowJob");

        JobExecution execution = new JobExecution(100L, jobInstance, new JobParameters());

        execution.setStatus(BatchStatus.COMPLETED);

        when(jobRepository.getJobNames()).thenReturn(List.of("dataWorkflowJob"));

        when(jobRepository.getJobInstances("dataWorkflowJob", 0, Integer.MAX_VALUE))
                .thenReturn(List.of(jobInstance));

        when(jobRepository.getJobExecutions(jobInstance)).thenReturn(List.of(execution));

        List<String> result = controller.getRunningSteps();

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnJobHistorySortedByExecutionIdDescending() {

        JobInstance jobInstance = new JobInstance(1L, "dataWorkflowJob");

        JobExecution oldestExecution = new JobExecution(10L, jobInstance, new JobParameters());

        oldestExecution.setStatus(BatchStatus.COMPLETED);

        JobExecution latestExecution = new JobExecution(20L, jobInstance, new JobParameters());

        latestExecution.setStatus(BatchStatus.COMPLETED);

        when(jobRepository.getJobNames()).thenReturn(List.of("dataWorkflowJob"));

        when(jobRepository.getJobInstances("dataWorkflowJob", 0, Integer.MAX_VALUE))
                .thenReturn(List.of(jobInstance));

        when(jobRepository.getJobExecutions(jobInstance)).thenReturn(List.of(oldestExecution, latestExecution));

        List<BatchJobStatusDto> result = controller.getJobHistory();

        assertThat(result).hasSize(2);

        assertThat(result.get(0).executionId()).isEqualTo(20L);

        assertThat(result.get(1).executionId()).isEqualTo(10L);
    }

    @Test
    void shouldReturnEmptyHistoryWhenNoJobExists() {

        when(jobRepository.getJobNames()).thenReturn(List.of());

        List<BatchJobStatusDto> result = controller.getJobHistory();

        assertThat(result).isEmpty();
    }
}
