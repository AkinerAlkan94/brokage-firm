package com.akiner.brokage_firm.exception;

public class IBANIsNotGivenException extends RuntimeException {
    public IBANIsNotGivenException(String message) {
        super(message);
    }
}
