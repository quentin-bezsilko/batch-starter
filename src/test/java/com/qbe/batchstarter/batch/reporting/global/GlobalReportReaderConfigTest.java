package com.qbe.batchstarter.batch.reporting.global;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.GlobalReportDto;
import com.qbe.batchstarter.repository.BankAccountRepository;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import com.qbe.batchstarter.repository.CustomerRepository;
import com.qbe.batchstarter.repository.FraudAlertRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.support.ListItemReader;

@ExtendWith(MockitoExtension.class)
class GlobalReportReaderConfigTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankTransactionRepository bankTransactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FraudAlertRepository fraudAlertRepository;

    @InjectMocks
    private GlobalReportReaderConfig config;

    @Test
    void shouldCreateGlobalReport() throws Exception {
        when(customerRepository.count()).thenReturn(100L);
        when(bankAccountRepository.count()).thenReturn(200L);
        when(bankTransactionRepository.count()).thenReturn(1000L);
        when(fraudAlertRepository.count()).thenReturn(10L);
        when(bankAccountRepository.totalBalance()).thenReturn(BigDecimal.valueOf(50000));

        ListItemReader<GlobalReportDto> reader = config.globalReportReader();

        GlobalReportDto report = reader.read();
        assertThat(report).isNotNull();
        assertThat(report.totalCustomers()).isEqualTo(100L);
        assertThat(report.totalAccounts()).isEqualTo(200L);
        assertThat(report.totalTransactions()).isEqualTo(1000L);
        assertThat(report.totalFrauds()).isEqualTo(10L);
        assertThat(report.totalBalance()).isEqualByComparingTo(BigDecimal.valueOf(50000));

        verify(customerRepository).count();
        verify(bankAccountRepository).count();
        verify(bankTransactionRepository).count();
        verify(fraudAlertRepository).count();
        verify(bankAccountRepository).totalBalance();
    }
}
