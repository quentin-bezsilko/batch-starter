package com.qbe.batchstarter.batch.importer.account;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.mapper.BankAccountMapper;
import com.qbe.batchstarter.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountProcessorConfig implements ItemProcessor<BankAccountDto, BankAccountEntity> {

    private final BankAccountMapper bankAccountMapper;
    private final CustomerRepository customerRepository;

    @Override
    public BankAccountEntity process(@NonNull BankAccountDto item) {
        BankAccountEntity entity = bankAccountMapper.toEntity(item);
        entity.setCustomer(customerRepository.getReferenceById(item.customerId()));
        return entity;
    }
}
