package com.akiner.brokage_firm.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IBANValidatorTest {

    @Test
    public void isValid_returnsTrue_whenIbanIsValid() {
        String validIban = "TG53TG0090604310346500400070";

        assertTrue(new IBANValidator().isValid(validIban));
    }

    @Test
    public void isValid_returnsFalse_whenIbanIsInvalid() {
        String invalidIban = "GB82WEST 1234569876543"; // One digit less

        assertFalse(IBANValidator.isValid(invalidIban));
    }
}
