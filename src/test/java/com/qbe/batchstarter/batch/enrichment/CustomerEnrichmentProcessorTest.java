package com.qbe.batchstarter.batch.enrichment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.CustomerEnrichmentDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.enums.TransactionType;
import com.qbe.batchstarter.repository.BankAccountRepository;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerEnrichmentProcessorTest {

    @Mock
    private BankAccountRepository accountRepository;

    @Mock
    private BankTransactionRepository transactionRepository;

    @InjectMocks
    private CustomerEnrichmentProcessor processor;

    @Test
    void shouldBuildCustomerSummary() {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        BankAccountEntity accountEntity1 = BankAccountEntity.builder()
                .id(10L)
                .balance(BigDecimal.valueOf(1000))
                .customer(customer)
                .build();
        BankAccountEntity accountEntity2 = BankAccountEntity.builder()
                .id(11L)
                .balance(BigDecimal.valueOf(500))
                .customer(customer)
                .build();

        when(accountRepository.findByCustomerId(1L)).thenReturn(List.of(accountEntity1, accountEntity2));
        when(transactionRepository.findByAccountId(10L))
                .thenReturn(List.of(
                        BankTransactionEntity.builder()
                                .id(1L)
                                .account(accountEntity1)
                                .transactionDate(LocalDateTime.of(2025, 1, 1, 10, 0))
                                .transactionType(TransactionType.CARD_PAYMENT)
                                .amount(BigDecimal.valueOf(200))
                                .build(),
                        BankTransactionEntity.builder()
                                .id(2L)
                                .account(accountEntity1)
                                .transactionDate(LocalDateTime.of(2025, 1, 2, 10, 0))
                                .transactionType(TransactionType.DIRECT_DEBIT)
                                .amount(BigDecimal.valueOf(-50))
                                .build()));
        when(transactionRepository.findByAccountId(11L))
                .thenReturn(List.of(
                        BankTransactionEntity.builder()
                                .id(3L)
                                .account(accountEntity2)
                                .transactionDate(LocalDateTime.of(2025, 1, 3, 10, 0))
                                .transactionType(TransactionType.CARD_PAYMENT)
                                .amount(BigDecimal.valueOf(300))
                                .build(),
                        BankTransactionEntity.builder()
                                .id(4L)
                                .account(accountEntity2)
                                .transactionDate(LocalDateTime.of(2025, 1, 4, 10, 0))
                                .transactionType(TransactionType.DIRECT_DEBIT)
                                .amount(BigDecimal.valueOf(-100))
                                .build()));

        CustomerEnrichmentDto result = processor.process(customer);
        assertThat(result).isNotNull();
        assertThat(result.customerId()).isEqualTo(1L);
        assertThat(result.customerName()).isEqualTo("John Doe");
        assertThat(result.accountCount()).isEqualTo(2);
        assertThat(result.totalBalance()).isEqualByComparingTo("1500");
        assertThat(result.transactionCount()).isEqualTo(4);
        assertThat(result.totalCredit()).isEqualByComparingTo("500");
        assertThat(result.totalDebit()).isEqualByComparingTo("150");
    }

    @Test
    void shouldHandleCustomerWithoutAccounts() {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        when(accountRepository.findByCustomerId(1L)).thenReturn(List.of());

        CustomerEnrichmentDto result = processor.process(customer);
        assertThat(result).isNotNull();
        assertThat(result.customerId()).isEqualTo(1L);
        assertThat(result.customerName()).isEqualTo("John Doe");
        assertThat(result.accountCount()).isZero();
        assertThat(result.transactionCount()).isZero();
        assertThat(result.totalBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.totalCredit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.totalDebit()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
