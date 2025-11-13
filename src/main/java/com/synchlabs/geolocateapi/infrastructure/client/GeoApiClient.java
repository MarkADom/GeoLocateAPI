package com.synchlabs.geolocateapi.infrastructure.client;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.application.port.out.GeoProviderPort;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.dto.IpApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GeoApiClient implements GeoProviderPort {

    private final RestTemplate restTemplate;

    @Value("${external.geo.base-url}")
    private String baseUrl;

    @Value("${external.geo.provider}")
    private String provider;


    public GeoApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GeoLocationData findByIp(String ip) {

        long start = System.currentTimeMillis();
        String url = baseUrl + "/json/" + ip;

        log.info("Calling {} provider for IP {}", provider, ip);

        IpApiResponse response;

        try {
            response = restTemplate.getForObject(url, IpApiResponse.class);
        } catch (Exception ex) {
            log.error("Provider {} failed for IP {}: {}", provider, ip, ex.getMessage());
            throw new ExternalServiceException("Failed to fetch geolocation for IP: " + ip);
        }

        long duration = System.currentTimeMillis() - start;

        if (duration > 500) {
            log.warn("Provider {} took {} ms for IP {}", provider, duration, ip);
        } else {
            log.info("Provide {} responded in {} ms for IP {}", provider, duration, ip);
        }

        if (response == null || !"success".equalsIgnoreCase(response.getStatus())) {
            log.error("Invalid provider response for IP {}: {}", provider, ip);
            throw new ExternalServiceException("Failed to fetch geolocation for IP: " + ip);
        }

        return new GeoLocationData(
                response.getLat(),
                response.getLon(),
                response.getCountry(),
                response.getCity(),
                response.getRegionName(),
                response.getTimezone(),
                response.getIsp(),
                response.getQuery(),    // ip
                provider         // source
        );
    }

    @Override
    public GeoLocationData findByCoordinates(double lat, double lon) {
        throw new UnsupportedOperationException("Reverse geocoding not implemented yet");
    }

    @Override
    public GeoLocationData findByCity(String city) {
        throw new UnsupportedOperationException("City lookup not implemented yet");
    }
}
