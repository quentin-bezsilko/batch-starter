package com.qbe.batchstarter.batch.reporting.customer;

import static org.assertj.core.api.Assertions.assertThat;

import com.qbe.batchstarter.dto.CustomerReportDto;
import com.qbe.batchstarter.dto.CustomerReportExportDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CustomerReportProcessorTest {

    private final CustomerReportProcessor processor = new CustomerReportProcessor();

    @Test
    void shouldCalculateAge() {
        CustomerReportDto input = new CustomerReportDto(
                1L, "John Doe", "FR", LocalDate.now().minusYears(30), 3L, BigDecimal.valueOf(1000));

        CustomerReportExportDto result = processor.process(input);
        assertThat(result).isNotNull();
        assertThat(result.age()).isEqualTo(30);
        assertThat(result.customerName()).isEqualTo("John Doe");
    }

    @Test
    void shouldHandleNullBirthDate() {
        CustomerReportDto input = new CustomerReportDto(1L, "John Doe", "FR", null, 3L, BigDecimal.valueOf(1000));

        CustomerReportExportDto result = processor.process(input);
        assertThat(result).isNotNull();
        assertThat(result.age()).isNull();
    }
}
