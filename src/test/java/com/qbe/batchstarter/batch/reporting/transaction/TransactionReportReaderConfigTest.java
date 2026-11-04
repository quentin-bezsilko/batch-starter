package com.qbe.batchstarter.batch.reporting.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.TransactionReportDto;
import com.qbe.batchstarter.enums.TransactionType;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.support.ListItemReader;

@ExtendWith(MockitoExtension.class)
class TransactionReportReaderConfigTest {

    @Mock
    private BankTransactionRepository repository;

    @InjectMocks
    private TransactionReportReaderConfig config;

    @Test
    void shouldCreateTransactionReportReader() {
        TransactionReportDto dto =
                new TransactionReportDto(TransactionType.TRANSFER, 10L, BigDecimal.valueOf(50000), 5000.0);
        when(repository.buildTransactionReport()).thenReturn(List.of(dto));

        ListItemReader<TransactionReportDto> reader = config.transactionReportReader();

        TransactionReportDto result = reader.read();
        assertThat(result).isEqualTo(dto);

        verify(repository).buildTransactionReport();
    }

    @Test
    void shouldReadAllReportRows() {
        List<TransactionReportDto> reports = List.of(
                new TransactionReportDto(TransactionType.TRANSFER, 10L, BigDecimal.valueOf(50000), 5000.0),
                new TransactionReportDto(TransactionType.CARD_PAYMENT, 5L, BigDecimal.valueOf(10000), 2000.0));

        when(repository.buildTransactionReport()).thenReturn(reports);

        ListItemReader<TransactionReportDto> reader = config.transactionReportReader();
        int count = 0;
        while (reader.read() != null) {
            count++;
        }
        assertThat(count).isEqualTo(2);
    }
}
