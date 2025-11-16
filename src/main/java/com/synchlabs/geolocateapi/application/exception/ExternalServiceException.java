package com.synchlabs.geolocateapi.application.exception;

/**
 * Exception representing a failure when communicating with an external provider.
 *
 * This is thrown when a provider returns an invalid response, times out, or
 * cannot be reached. The application layer uses this exception to signal
 * provider-side problems without coupling to HTTP or client-specific details.
 */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
