package com.akiner.brokage_firm.exception;

public class StockOrderNotFoundException extends RuntimeException {
    public StockOrderNotFoundException(String message) {
        super(message);
    }
}
