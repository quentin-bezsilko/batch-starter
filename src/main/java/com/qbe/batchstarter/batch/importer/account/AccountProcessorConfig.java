package com.qbe.batchstarter.batch.importer.account;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.exception.InvalidBankAccountException;
import com.qbe.batchstarter.exception.TemporaryException;
import com.qbe.batchstarter.mapper.BankAccountMapper;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountProcessorConfig implements ItemProcessor<BankAccountDto, BankAccountEntity> {

    private final BankAccountMapper bankAccountMapper;
    private final CustomerRepository customerRepository;
    private final GeneratorProperties generatorProperties;
    private final AccountGeneratorProperties accountGeneratorProperties;

    private boolean retryDone;
    private long processed;

    @Override
    public BankAccountEntity process(@NonNull BankAccountDto item) {
        processed++;

        if (accountGeneratorProperties.crashAfter() > 0 && processed == accountGeneratorProperties.crashAfter()) {
            throw new RuntimeException("Crash volontaire");
        }
        if (generatorProperties.simulateRetry() && !retryDone) {
            log.info("ERREUR TEMPORAIRE SIMULEE for item with id={}", item.id());
            retryDone = true;
            throw new TemporaryException("Erreur temporaire simulée");
        }
        if (item.iban() == null || item.iban().isBlank()) {
            log.info("IBAN manquant for item with id={}", item.id());
            throw new InvalidBankAccountException("IBAN manquant");
        }

        BankAccountEntity entity = bankAccountMapper.toEntity(item);
        entity.setCustomer(customerRepository.getReferenceById(item.customerId()));
        return entity;
    }
}
