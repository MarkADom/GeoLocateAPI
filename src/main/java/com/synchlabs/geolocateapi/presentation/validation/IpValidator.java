package com.synchlabs.geolocateapi.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IpValidator implements ConstraintValidator<ValidIp, String> {

    private static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." +
                    "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." +
                    "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\." +
                    "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

       if (value == null) return false;

       return value.matches(IPV4_REGEX);
    }
}
