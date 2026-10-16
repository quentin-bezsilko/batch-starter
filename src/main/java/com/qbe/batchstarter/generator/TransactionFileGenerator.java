package com.qbe.batchstarter.generator;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.dto.MerchantReference;
import com.qbe.batchstarter.dto.TransactionDto;
import com.qbe.batchstarter.service.RandomReferenceService;
import com.qbe.batchstarter.service.TransactionGeneratorService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionFileGenerator {

    private final TransactionGeneratorService transactionGeneratorService;
    private final RandomReferenceService randomReferenceService;

    private final Random random = new Random();

    public List<TransactionDto> generate(List<BankAccountDto> accounts, int transactionCount) {
        List<TransactionDto> transactions = new ArrayList<>(transactionCount);
        for (long transactionId = 1; transactionId <= transactionCount; transactionId++) {
            BankAccountDto account = accounts.get(random.nextInt(accounts.size()));
            MerchantReference merchant = randomReferenceService.randomMerchant();
            transactions.add(transactionGeneratorService.generate(transactionId, account, merchant));
        }
        return transactions;
    }
}
