package com.synchlabs.geolocateapi.infrastructure.adapters;

import com.synchlabs.geolocateapi.application.port.out.GeoProviderPort;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.ip.IpGeoClient;
import com.synchlabs.geolocateapi.infrastructure.client.reverse.ReverseGeoClient;
import com.synchlabs.geolocateapi.infrastructure.client.search.CitySearchClient;
import org.springframework.stereotype.Component;

/**
 * Composite adapter implementing {@link GeoProviderPort} by delegating
 * geolocation operations to specific provider clients.
 *
 * This adapter acts as the bridge between the application layer and
 * the different external provider adapters.
 *
 * Responsibilities:
 * - Coordinate IP, reverse geocoding and city-search providers.
 * - Map external DTOs into the domain {@link GeoLocationData}.
 * - Encapsulate all provider-specific logic behind the hexagonal port.
 *
 * This class ensures that adding or replacing providers does not
 * affect the application or domain layers.
 */
@Component
public class GeoProviderCompositeAdapter implements GeoProviderPort {

    private final IpGeoClient ipGeoClient;
    private final ReverseGeoClient reverseGeoClient;
    private final CitySearchClient citySearchClient;

    public GeoProviderCompositeAdapter(
            IpGeoClient ipGeoClient,
            ReverseGeoClient reverseGeoClient,
            CitySearchClient citySearchClient
    ) {
        this.ipGeoClient = ipGeoClient;
        this.reverseGeoClient = reverseGeoClient;
        this.citySearchClient = citySearchClient;
    }

    @Override
    public GeoLocationData findByIp(String ip) {
        return ipGeoClient.findByIp(ip);
    }

    @Override
    public GeoLocationData findByCoordinates(double lat, double lon) {
        return reverseGeoClient.findByCoordinates(lat, lon);
    }

    @Override
    public GeoLocationData findByCity(String city) {
        return citySearchClient.findByCity(city);
    }
}
