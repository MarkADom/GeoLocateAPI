package com.synchlabs.geolocateapi.application.port.out;

import com.synchlabs.geolocateapi.domain.model.GeoLocationData;

/**
 * Hexagonal port abstracting the access to external geolocation providers.
 *
 * Infrastructure adapters implement this interface, allowing the application layer
 * to remain independent of specific provider logic or HTTP details.
 */
public interface GeoProviderPort {
    GeoLocationData findByCoordinates(double lat, double lon);
    GeoLocationData findByCity(String city);
    GeoLocationData findByIp(String ip);
}
