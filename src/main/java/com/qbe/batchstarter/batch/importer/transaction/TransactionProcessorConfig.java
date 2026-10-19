package com.qbe.batchstarter.batch.importer.transaction;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.mapper.BankTransactionMapper;
import com.qbe.batchstarter.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionProcessorConfig implements ItemProcessor<BankTransactionDto, BankTransactionEntity> {

    private final BankTransactionMapper bankTransactionMapper;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public BankTransactionEntity process(@NonNull BankTransactionDto bankTransactionDto) {
        BankTransactionEntity entity = bankTransactionMapper.toEntity(bankTransactionDto);
        entity.setAccount(bankAccountRepository.getReferenceById(bankTransactionDto.accountId()));
        return entity;
    }
}
