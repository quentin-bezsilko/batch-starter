package com.qbe.batchstarter.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.service.CustomerGeneratorService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class CustomerFileGeneratorTest {

    @Test
    void shouldGenerateExpectedNumberOfCustomers() {
        CustomerGeneratorService generatorService = mock(CustomerGeneratorService.class);

        when(generatorService.generate(1L))
                .thenReturn(new CustomerDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "FR"));
        when(generatorService.generate(2L))
                .thenReturn(new CustomerDto(2L, "Jane", "Doe", LocalDate.of(1992, 1, 1), "FR"));

        CustomerFileGenerator generator = new CustomerFileGenerator(generatorService);
        List<CustomerDto> customers = generator.generate(2);

        assertThat(customers).hasSize(2);
        verify(generatorService).generate(1L);
        verify(generatorService).generate(2L);
    }

    @Test
    void shouldReturnEmptyListWhenCountIsZero() {
        CustomerGeneratorService generatorService = mock(CustomerGeneratorService.class);
        CustomerFileGenerator generator = new CustomerFileGenerator(generatorService);
        List<CustomerDto> customers = generator.generate(0);

        assertThat(customers).isEmpty();
        verifyNoInteractions(generatorService);
    }
}
