package com.qbe.batchstarter.mapper;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankTransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "account", ignore = true)
    BankTransactionEntity toEntity(BankTransactionDto dto);
}
