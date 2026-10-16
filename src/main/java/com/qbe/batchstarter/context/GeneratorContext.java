package com.qbe.batchstarter.context;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.CustomerDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GeneratorContext {

    private List<CustomerDto> customers;

    private List<BankAccountDto> accounts;
}
