package com.qbe.batchstarter.mapper;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    CustomerEntity toEntity(CustomerDto dto);
}
