package com.synchlabs.geolocateapi.presentation.error;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Standard API error response returned by the presentation layer.
 *
 * Used by {@link com.synchlabs.geolocateapi.application.exception.GlobalExceptionHandler}
 * to ensure that all application and provider errors follow a consistent JSON structure.
 *
 * This model is intentionally presentation-specific and should not leak
 * back into the domain or application layers.
 */
@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
