package com.qbe.batchstarter.batch.reporting.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.CustomerReportDto;
import com.qbe.batchstarter.repository.CustomerRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.support.ListItemReader;

@ExtendWith(MockitoExtension.class)
class CustomerReportReaderConfigTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerReportReaderConfig config;

    @Test
    void shouldCreateReader() {
        List<CustomerReportDto> reports =
                List.of(new CustomerReportDto(1L, "John Doe", "FR", LocalDate.of(1990, 1, 1), 2L, BigDecimal.TEN));
        when(repository.findCustomerReport()).thenReturn(reports);

        ListItemReader<CustomerReportDto> reader = config.customerReportReader();
        assertThat(reader.read()).isNotNull();

        verify(repository).findCustomerReport();
    }
}
