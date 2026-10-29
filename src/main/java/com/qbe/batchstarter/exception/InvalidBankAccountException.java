package com.qbe.batchstarter.exception;

public class InvalidBankAccountException extends RuntimeException {

    public InvalidBankAccountException(String message) {
        super(message);
    }
}
