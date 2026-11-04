package com.qbe.batchstarter.batch.reporting.bestclients;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BestClientReportDto;
import com.qbe.batchstarter.repository.CustomerRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.support.ListItemReader;

@ExtendWith(MockitoExtension.class)
class BestClientReportReaderConfigTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BestClientReportReaderConfig config;

    @Test
    void shouldReadTop20Clients() {
        List<BestClientReportDto> clients =
                List.of(new BestClientReportDto(1L, "John Doe", 5L, BigDecimal.valueOf(1000)));
        when(customerRepository.findBestClients()).thenReturn(clients);

        ListItemReader<BestClientReportDto> reader = config.bestClientReportReader();
        verify(customerRepository).findBestClients();
    }

    @Test
    void shouldLimitTo20Clients() {
        List<BestClientReportDto> clients = java.util.stream.LongStream.range(0, 30)
                .mapToObj(i -> new BestClientReportDto(i, "Client " + i, 1L, BigDecimal.TEN))
                .toList();

        when(customerRepository.findBestClients()).thenReturn(clients);

        ListItemReader<BestClientReportDto> reader = config.bestClientReportReader();
        int count = 0;
        while (reader.read() != null) {
            count++;
        }
        org.assertj.core.api.Assertions.assertThat(count).isEqualTo(20);
    }
}
