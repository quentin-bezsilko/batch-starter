package com.qbe.batchstarter.batch.reporting.fraud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.FraudReportDto;
import com.qbe.batchstarter.repository.FraudAlertRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.support.ListItemReader;

@ExtendWith(MockitoExtension.class)
class FraudReportReaderConfigTest {

    @Mock
    private FraudAlertRepository fraudAlertRepository;

    @InjectMocks
    private FraudReportReaderConfig config;

    @Test
    void shouldCreateReader() {
        List<FraudReportDto> report = List.of(new FraudReportDto("HIGH_AMOUNT", 5L, BigDecimal.valueOf(100000)));
        when(fraudAlertRepository.buildFraudReport()).thenReturn(report);

        ListItemReader<FraudReportDto> reader = config.fraudReportReader();

        FraudReportDto item = reader.read();
        assertThat(item).isNotNull();
        verify(fraudAlertRepository).buildFraudReport();
    }
}
