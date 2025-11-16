package com.synchlabs.geolocateapi.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation used to validate IPv4 address fields.
 *
 * Applied to presentation-layer inputs to prevent invalid IP formats
 * from reaching the application or infrastructure layers.
 *
 * Validation logic is implemented in {@link IpValidator}.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IpValidator.class)
public @interface ValidIp {

    String message() default "Invalid IP format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
