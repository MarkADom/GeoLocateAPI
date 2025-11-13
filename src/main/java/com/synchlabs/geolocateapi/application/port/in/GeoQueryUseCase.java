package com.synchlabs.geolocateapi.application.port.in;

import com.synchlabs.geolocateapi.domain.model.GeoLocationData;

public interface GeoQueryUseCase {
    GeoLocationData findByCoordinates(double lat, double lon);
    GeoLocationData findByCity(String city);
    GeoLocationData findByIp(String ip);
}
