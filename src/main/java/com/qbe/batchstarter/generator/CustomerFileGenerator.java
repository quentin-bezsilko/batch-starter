package com.qbe.batchstarter.generator;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.service.CustomerGeneratorService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerFileGenerator {

    private final CustomerGeneratorService customerGeneratorService;

    public List<CustomerDto> generate(int count) {
        List<CustomerDto> customers = new ArrayList<>(count);
        for (long i = 1; i <= count; i++) {
            customers.add(customerGeneratorService.generate(i));
        }
        return customers;
    }
}
