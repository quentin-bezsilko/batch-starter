package com.qbe.batchstarter.batch.importer.account;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.exception.InvalidBankAccountException;
import com.qbe.batchstarter.exception.TemporaryException;
import com.qbe.batchstarter.mapper.BankAccountMapper;
import com.qbe.batchstarter.properties.AccountGeneratorProperties;
import com.qbe.batchstarter.properties.GeneratorProperties;
import com.qbe.batchstarter.repository.CustomerRepository;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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

    private final AtomicBoolean retryDone = new AtomicBoolean();
    private final AtomicInteger processed = new AtomicInteger();

    @Override
    public BankAccountEntity process(@NonNull BankAccountDto item) {
        int processedLong = processed.incrementAndGet();
        if (accountGeneratorProperties.crashAfter() > 0 && processedLong == accountGeneratorProperties.crashAfter()) {
            throw new RuntimeException("Crash volontaire");
        }
        if (generatorProperties.simulateRetry() && retryDone.compareAndSet(false, true)) {
            log.info("Erreur temporaire simulée for item with id={}", item.id());
            throw new TemporaryException("Erreur temporaire simulée");
        }
        if (item.iban() == null || item.iban().isBlank()) {
            log.info("Iban manquant for item with id={}", item.id());
            throw new InvalidBankAccountException("Iban manquant");
        }

        BankAccountEntity entity = bankAccountMapper.toEntity(item);
        entity.setCustomer(customerRepository.getReferenceById(item.customerId()));
        return entity;
    }
}
