package com.qbe.batchstarter.batch.importer.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.mapper.CustomerMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CustomerProcessorTest {

    @Test
    void shouldMapCustomerDtoToEntity() {
        CustomerMapper mapper = mock(CustomerMapper.class);
        CustomerEntity entity = new CustomerEntity();

        when(mapper.toEntity(any(CustomerDto.class))).thenReturn(entity);

        CustomerProcessor processor = new CustomerProcessor(mapper);
        CustomerDto dto = new CustomerDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "FR");

        CustomerEntity result = processor.process(dto);
        assertThat(result).isSameAs(entity);
        verify(mapper).toEntity(dto);
    }
}
