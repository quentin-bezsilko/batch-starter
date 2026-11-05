package com.qbe.batchstarter.batch.enrichment;

import com.qbe.batchstarter.dto.CustomerEnrichmentDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.metrics.Monitored;
import com.qbe.batchstarter.repository.BankAccountRepository;
import com.qbe.batchstarter.repository.BankTransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerEnrichmentProcessor implements ItemProcessor<CustomerEntity, CustomerEnrichmentDto> {

    private final BankAccountRepository accountRepository;
    private final BankTransactionRepository transactionRepository;

    @Override
    @Monitored(name = "customer.enrichment.processor")
    public CustomerEnrichmentDto process(CustomerEntity customer) {
        List<BankAccountEntity> accounts = accountRepository.findByCustomerId(customer.getId());

        BigDecimal totalBalance = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        long transactionCount = 0;

        List<BankTransactionEntity> transactions;
        BigDecimal amount;
        for (BankAccountEntity account : accounts) {
            totalBalance = totalBalance.add(account.getBalance());
            transactions = transactionRepository.findByAccountId(account.getId());
            transactionCount += transactions.size();
            for (BankTransactionEntity transaction : transactions) {
                amount = transaction.getAmount();
                if (amount.signum() >= 0) {
                    totalCredit = totalCredit.add(amount);
                } else {
                    totalDebit = totalDebit.add(amount.abs());
                }
            }
        }

        return new CustomerEnrichmentDto(
                customer.getId(),
                customer.getFirstName() + " " + customer.getLastName(),
                accounts.size(),
                totalBalance,
                transactionCount,
                totalCredit,
                totalDebit);
    }
}
