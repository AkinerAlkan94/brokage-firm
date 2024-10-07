package com.akiner.brokage_firm.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionTest {
    public <T extends Exception> void testException(Class<T> exceptionClass, Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            assertEquals(exceptionClass, e.getClass());
        }
    }

    @Test
    public void testException() {
        testException(
                AssetNotFoundException.class,
                () -> {
                    throw new AssetNotFoundException("");
                });
        testException(
                CustomerAlreadyExistsException.class,
                () -> {
                    throw new CustomerAlreadyExistsException("");
                });
        testException(
                CustomerNotFoundException.class,
                () -> {
                    throw new CustomerNotFoundException("");
                });
        testException(
                IBANIsNotGivenException.class,
                () -> {
                    throw new IBANIsNotGivenException("");
                });
        testException(
                InsufficientBalanceException.class,
                () -> {
                    throw new InsufficientBalanceException("");
                });
        testException(
                InsufficientAssetBalanceException.class,
                () -> {
                    throw new InsufficientAssetBalanceException("");
                });
        testException(
                InvalidIBANIsGivenException.class,
                () -> {
                    throw new InvalidIBANIsGivenException("");
                });
        testException(
                StockOrderIsNotInPendingStateException.class,
                () -> {
                    throw new StockOrderIsNotInPendingStateException("");
                });
        testException(
                StockOrderNotFoundException.class,
                () -> {
                    throw new StockOrderNotFoundException("");
                });
    }
}
