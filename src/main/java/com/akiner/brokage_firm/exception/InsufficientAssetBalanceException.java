package com.akiner.brokage_firm.exception;

public class InsufficientAssetBalanceException extends RuntimeException {
    public InsufficientAssetBalanceException(String message) {
        super(message);
    }
}
