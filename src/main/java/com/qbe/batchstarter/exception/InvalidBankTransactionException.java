package com.qbe.batchstarter.exception;

public class InvalidBankTransactionException extends RuntimeException {

    public InvalidBankTransactionException(String message) {
        super(message);
    }
}
