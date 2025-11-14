package com.synchlabs.geolocateapi.infrastructure.adapters;

import com.synchlabs.geolocateapi.application.port.out.GeoProviderPort;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.ip.IpGeoClient;
import com.synchlabs.geolocateapi.infrastructure.client.reverse.ReverseGeoClient;
import com.synchlabs.geolocateapi.infrastructure.client.search.CitySearchClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
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
    @Cacheable(cacheNames = "geo-ip", key = "#ip")
    public GeoLocationData findByIp(String ip) {
        log.info("[CACHE MISS] geo-ip → {}", ip);
        return ipGeoClient.findByIp(ip);
    }

    @Override
    @Cacheable(
            cacheNames = "geo-coords",
            key = "T(java.lang.String).format('%s:%s', #lat, #lon)"
    )
    public GeoLocationData findByCoordinates(double lat, double lon) {
        log.info("[CACHE MISS] geo-coords → {}:{}", lat, lon);
        return reverseGeoClient.findByCoordinates(lat, lon);
    }

    @Override
    @Cacheable(cacheNames = "geo-city", key = "#city.toLowerCase()")
    public GeoLocationData findByCity(String city) {
        log.info("[CACHE MISS] geo-city → {}", city);
        return citySearchClient.findByCity(city);
    }
}
