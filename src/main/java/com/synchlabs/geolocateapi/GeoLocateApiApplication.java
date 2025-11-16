package com.synchlabs.geolocateapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the GeoLocateAPI Spring Boot application.
 *
 * This class only bootstraps the application context.
 * It contains no business logic and should remain framework-specific.
 */
@SpringBootApplication
public class GeoLocateApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeoLocateApiApplication.class, args);
    }
}
