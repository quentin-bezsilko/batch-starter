package com.qbe.batchstarter.batch.importer.customer;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerProcessorConfig implements ItemProcessor<CustomerDto, CustomerEntity> {

    private final CustomerMapper customerMapper;

    @Override
    public CustomerEntity process(@NonNull CustomerDto customer) {
        return customerMapper.toEntity(customer);
    }
}
