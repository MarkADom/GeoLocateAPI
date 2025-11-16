package com.synchlabs.geolocateapi.application.service;

import com.synchlabs.geolocateapi.application.port.in.GeoQueryUseCase;
import com.synchlabs.geolocateapi.application.port.out.GeoProviderPort;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import org.springframework.stereotype.Service;

/**
 * Application service responsible for orchestrating geolocation operations.
 *
 * Delegates provider-specific logic to implementations of {@link GeoProviderPort}.
 * Converts provider output into the domain {@link GeoLocationData} model.
 *
 * This class contains the core application flow and must remain free of framework
 * and infrastructure concerns.
 */
@Service
public class GeoService implements GeoQueryUseCase {

    private final GeoProviderPort geoProviderPort;

    public GeoService(GeoProviderPort geoProviderPort) {
        this.geoProviderPort = geoProviderPort;
    }

    @Override
    public GeoLocationData findByCoordinates(double lat, double lon)   {
        return geoProviderPort.findByCoordinates(lat, lon);
    }

    @Override
    public GeoLocationData findByCity(String city) {
        return geoProviderPort.findByCity(city);
    }

    @Override
    public GeoLocationData findByIp(String ip) {
        return geoProviderPort.findByIp(ip);
    }
}
