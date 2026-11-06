package com.qbe.batchstarter.generator;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.dto.MerchantReference;
import com.qbe.batchstarter.service.RandomReferenceService;
import com.qbe.batchstarter.service.TransactionGeneratorService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankTransactionFileGenerator {

    private final TransactionGeneratorService transactionGeneratorService;
    private final RandomReferenceService randomReferenceService;
    private final SecureRandom random;

    public List<BankTransactionDto> generate(List<BankAccountDto> accounts, int transactionCount) {
        List<BankAccountDto> validAccounts = accounts.stream()
                .filter(a -> a.iban() != null && !a.iban().isBlank())
                .toList();

        if (validAccounts.isEmpty()) {
            throw new IllegalArgumentException("No valid accounts available to generate transactions");
        }

        List<BankTransactionDto> transactions = new ArrayList<>(transactionCount);
        for (long transactionId = 1; transactionId <= transactionCount; transactionId++) {
            BankAccountDto account = validAccounts.get(random.nextInt(validAccounts.size()));
            MerchantReference merchant = randomReferenceService.randomMerchant();
            transactions.add(transactionGeneratorService.generate(transactionId, account, merchant));
        }
        return transactions;
    }
}
