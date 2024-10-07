package com.akiner.brokage_firm.exception;

public class StockOrderIsNotInPendingStateException extends RuntimeException {
    public StockOrderIsNotInPendingStateException(String message) {
        super(message);
    }
}
