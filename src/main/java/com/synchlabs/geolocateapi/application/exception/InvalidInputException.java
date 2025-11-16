package com.synchlabs.geolocateapi.application.exception;

/**
 * Exception thrown when client input does not meet validation requirements.
 *
 * This exception is raised during input parsing or validation and is converted
 * by the global exception handler into a meaningful 400 Bad Request response.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}
