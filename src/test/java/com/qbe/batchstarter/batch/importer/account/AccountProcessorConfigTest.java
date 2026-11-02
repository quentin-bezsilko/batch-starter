package com.qbe.batchstarter.batch.importer.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import com.qbe.batchstarter.exception.InvalidBankAccountException;
import com.qbe.batchstarter.exception.TemporaryException;
import com.qbe.batchstarter.mapper.BankAccountMapper;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.repository.CustomerRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class AccountProcessorConfigTest {

    @Test
    void shouldMapAndAssignCustomer() {
        BankAccountMapper mapper = mock(BankAccountMapper.class);
        CustomerRepository repository = mock(CustomerRepository.class);
        GeneratorProperties generatorProperties = mock(GeneratorProperties.class);
        AccountGeneratorProperties accountProperties = mock(AccountGeneratorProperties.class);

        when(generatorProperties.simulateRetry()).thenReturn(false);
        when(accountProperties.crashAfter()).thenReturn(0);

        CustomerEntity customer = mock(CustomerEntity.class);
        when(repository.getReferenceById(10L)).thenReturn(customer);

        BankAccountEntity entity = new BankAccountEntity();
        when(mapper.toEntity(any())).thenReturn(entity);

        AccountProcessorConfig processor =
                new AccountProcessorConfig(mapper, repository, generatorProperties, accountProperties);
        BankAccountDto dto = new BankAccountDto(
                1L, 10L, "FR123", AccountType.CHECKING, "EUR", BigDecimal.TEN, AccountStatus.ACTIVE, LocalDate.now());
        BankAccountEntity result = processor.process(dto);

        assertThat(result).isSameAs(entity);
        assertNotNull(result);
        assertThat(result.getCustomer()).isSameAs(customer);
    }

    @Test
    void shouldThrowInvalidBankAccountExceptionWhenIbanIsBlank() {
        AccountProcessorConfig processor = new AccountProcessorConfig(
                mock(BankAccountMapper.class),
                mock(CustomerRepository.class),
                mock(GeneratorProperties.class),
                mock(AccountGeneratorProperties.class));

        BankAccountDto dto = new BankAccountDto(
                1L, 10L, "", AccountType.CHECKING, "EUR", BigDecimal.TEN, AccountStatus.ACTIVE, LocalDate.now());

        assertThatThrownBy(() -> processor.process(dto))
                .isInstanceOf(InvalidBankAccountException.class)
                .hasMessage("Iban manquant");
    }

    @Test
    void shouldThrowTemporaryExceptionOnlyOnce() {
        BankAccountMapper mapper = mock(BankAccountMapper.class);
        CustomerRepository repository = mock(CustomerRepository.class);
        GeneratorProperties generatorProperties = mock(GeneratorProperties.class);
        AccountGeneratorProperties accountProperties = mock(AccountGeneratorProperties.class);

        when(generatorProperties.simulateRetry()).thenReturn(true);
        when(accountProperties.crashAfter()).thenReturn(0);

        AccountProcessorConfig processor =
                new AccountProcessorConfig(mapper, repository, generatorProperties, accountProperties);
        BankAccountDto dto = new BankAccountDto(
                1L, 1L, "FR123", AccountType.CHECKING, "EUR", BigDecimal.ONE, AccountStatus.ACTIVE, LocalDate.now());
        assertThatThrownBy(() -> processor.process(dto)).isInstanceOf(TemporaryException.class);

        BankAccountEntity entity = new BankAccountEntity();
        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.getReferenceById(anyLong())).thenReturn(mock(CustomerEntity.class));

        assertThat(processor.process(dto)).isNotNull();
    }

    @Test
    void shouldCrashAfterConfiguredCount() {
        BankAccountMapper mapper = mock(BankAccountMapper.class);
        CustomerRepository repository = mock(CustomerRepository.class);
        GeneratorProperties generatorProperties = mock(GeneratorProperties.class);
        AccountGeneratorProperties accountProperties = mock(AccountGeneratorProperties.class);

        when(generatorProperties.simulateRetry()).thenReturn(false);
        when(accountProperties.crashAfter()).thenReturn(2);
        when(mapper.toEntity(any())).thenReturn(new BankAccountEntity());
        when(repository.getReferenceById(anyLong())).thenReturn(mock(CustomerEntity.class));

        AccountProcessorConfig processor =
                new AccountProcessorConfig(mapper, repository, generatorProperties, accountProperties);
        BankAccountDto dto = new BankAccountDto(
                1L, 1L, "FR123", AccountType.CHECKING, "EUR", BigDecimal.ONE, AccountStatus.ACTIVE, LocalDate.now());
        processor.process(dto);

        assertThatThrownBy(() -> processor.process(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Crash volontaire");
    }
}
