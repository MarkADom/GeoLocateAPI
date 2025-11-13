package com.synchlabs.geolocateapi.application.port.out;

import com.synchlabs.geolocateapi.domain.model.GeoLocationData;

public interface GeoProviderPort {
    GeoLocationData findByCoordinates(double lat, double lon);
    GeoLocationData findByCity(String city);
    GeoLocationData findByIp(String ip);
}
