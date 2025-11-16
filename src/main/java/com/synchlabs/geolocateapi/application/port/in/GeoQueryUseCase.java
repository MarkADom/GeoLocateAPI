package com.synchlabs.geolocateapi.application.port.in;

import com.synchlabs.geolocateapi.domain.model.GeoLocationData;


/**
 * Application-level input port defining the available geolocation queries.
 *
 * This port abstracts the use cases exposed by the application layer.
 * Controllers or other entrypoints invoke this interface instead of the service implementation.
 */
public interface GeoQueryUseCase {
    GeoLocationData findByCoordinates(double lat, double lon);
    GeoLocationData findByCity(String city);
    GeoLocationData findByIp(String ip);
}
