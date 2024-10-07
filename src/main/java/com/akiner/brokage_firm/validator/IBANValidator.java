package com.akiner.brokage_firm.validator;

import java.util.regex.Pattern;

public class IBANValidator {
    private static final Pattern IBAN_PATTERN =
            Pattern.compile("^([A-Z]{2})([0-9]{2})([A-Z0-9]{4})([0-9]{7})([A-Z0-9]{1,16})$");

    public static boolean isValid(String iban) {
        return IBAN_PATTERN.matcher(iban).matches();
    }
}
