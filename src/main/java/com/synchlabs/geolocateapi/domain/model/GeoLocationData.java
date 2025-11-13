package com.synchlabs.geolocateapi.domain.model;

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


