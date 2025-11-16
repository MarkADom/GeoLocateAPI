package com.synchlabs.geolocateapi.domain.model;

/**
 * Domain model representing a unified geolocation result.
 *
 * This object is framework-agnostic and contains no infrastructure details.
 * All external provider data is mapped into this model to keep the domain pure.
 */
public record GeoLocationData(
        double latitude,
        double longitude,
        String country,
        String city,
        String region,
        String timezone,
        String org,
        String ip,        // optional (when using GeoIP)
        String source     // "ip-api", "open-meteo", etc.
) {
}


