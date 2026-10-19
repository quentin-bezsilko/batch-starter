package com.qbe.batchstarter.mapper;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    BankAccountEntity toEntity(BankAccountDto dto);
}
