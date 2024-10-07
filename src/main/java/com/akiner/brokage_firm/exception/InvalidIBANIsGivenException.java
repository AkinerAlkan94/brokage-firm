package com.akiner.brokage_firm.exception;

public class InvalidIBANIsGivenException extends RuntimeException {
    public InvalidIBANIsGivenException(String message) {
        super(message);
    }
}
