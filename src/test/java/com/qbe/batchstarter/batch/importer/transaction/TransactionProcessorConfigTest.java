package com.qbe.batchstarter.batch.importer.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import com.qbe.batchstarter.exception.InvalidBankTransactionException;
import com.qbe.batchstarter.mapper.BankTransactionMapper;
import com.qbe.batchstarter.repository.BankAccountRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TransactionProcessorConfigTest {

    @Test
    void shouldMapAndAssignAccount() {
        BankTransactionMapper mapper = mock(BankTransactionMapper.class);
        BankAccountRepository repository = mock(BankAccountRepository.class);
        TransactionProcessorConfig processor = new TransactionProcessorConfig(mapper, repository);

        BankTransactionDto dto = new BankTransactionDto(
                1L,
                "FR123",
                LocalDateTime.now(),
                TransactionType.TRANSFER,
                BigDecimal.TEN,
                "Amazon",
                "EUR",
                TransactionStatus.COMPLETED);

        BankTransactionEntity entity = new BankTransactionEntity();
        BankAccountEntity account = new BankAccountEntity();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.findByIban("FR123")).thenReturn(Optional.of(account));

        BankTransactionEntity result = processor.process(dto);
        assertNotNull(result);
        assertThat(result).isSameAs(entity);
        assertThat(result.getAccount()).isSameAs(account);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        BankTransactionMapper mapper = mock(BankTransactionMapper.class);
        BankAccountRepository repository = mock(BankAccountRepository.class);

        TransactionProcessorConfig processor = new TransactionProcessorConfig(mapper, repository);

        BankTransactionDto dto = new BankTransactionDto(
                1L,
                "UNKNOWN",
                LocalDateTime.now(),
                TransactionType.TRANSFER,
                BigDecimal.TEN,
                "Amazon",
                "EUR",
                TransactionStatus.COMPLETED);

        when(mapper.toEntity(dto)).thenReturn(new BankTransactionEntity());

        when(repository.findByIban("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> processor.process(dto))
                .isInstanceOf(InvalidBankTransactionException.class)
                .hasMessage("Account not found for IBAN UNKNOWN");
    }
}
