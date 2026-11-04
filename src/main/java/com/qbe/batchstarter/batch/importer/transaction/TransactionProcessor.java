package com.qbe.batchstarter.batch.importer.transaction;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.exception.InvalidBankTransactionException;
import com.qbe.batchstarter.mapper.BankTransactionMapper;
import com.qbe.batchstarter.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionProcessor implements ItemProcessor<BankTransactionDto, BankTransactionEntity> {

    private final BankTransactionMapper bankTransactionMapper;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public BankTransactionEntity process(@NonNull BankTransactionDto dto) throws InvalidBankTransactionException {
        BankTransactionEntity entity = bankTransactionMapper.toEntity(dto);
        BankAccountEntity account = bankAccountRepository
                .findByIban(dto.accountIban())
                .orElseThrow(
                        () -> new InvalidBankTransactionException("Account not found for IBAN " + dto.accountIban()));
        entity.setAccount(account);
        return entity;
    }
}
