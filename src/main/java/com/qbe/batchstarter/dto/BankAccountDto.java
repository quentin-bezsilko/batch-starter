package com.qbe.batchstarter.dto;

import com.qbe.batchstarter.enums.AccountStatus;
import com.qbe.batchstarter.enums.AccountType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record BankAccountDto(
        Long id,
        Long customerId,
        String iban,
        AccountType accountType,
        String currency,
        BigDecimal balance,
        AccountStatus accountStatus,
        LocalDate openingDate) {}
