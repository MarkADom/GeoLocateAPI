package com.synchlabs.geolocateapi.infrastructure.client.reverse;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.reverse.dto.ReverseGeoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ReverseGeoClient {

    private final RestTemplate restTemplate;

    @Value("${external.providers.nominatim.reverse-url}")
    private String reverseUrl;

    public ReverseGeoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoLocationData findByCoordinates(double lat, double lon) {

        long start = System.currentTimeMillis();

        String url = reverseUrl
                + "?lat=" + lat
                + "&lon=" + lon
                + "&format=json"
                + "&addressdetails=1"
                + "&zoom=12";

        log.info("Calling Nominatim reverse provider for {}, {}", lat, lon);
        log.debug("Reverse Geo URL: {}", url);

        ReverseGeoResponse response;

        try {
            response = restTemplate.getForObject(url, ReverseGeoResponse.class);
        } catch (Exception e) {
            log.error("Reverse provider failed for {}, {}: {}", lat, lon, e.getMessage());
            throw new ExternalServiceException("Failed to reverse geocode coordinates: " + lat + ", " + lon);
        }

        long duration = System.currentTimeMillis() - start;
        log.info("Nominatim responded in {} ms", duration);

        if (response == null || response.getAddress() == null) {
            log.error("No address returned by Nominatim for {}, {}", lat, lon);
            throw new ExternalServiceException("Failed to reverse geocode coordinates: " + lat + ", " + lon);
        }

        var addr = response.getAddress();

        String city =
                addr.getCity() != null ? addr.getCity() :
                        addr.getTown() != null ? addr.getTown() :
                                addr.getVillage() != null ? addr.getVillage() :
                                        null;

        return new GeoLocationData(
                Double.parseDouble(response.getLat()),
                Double.parseDouble(response.getLon()),
                addr.getCountry(),
                city,
                addr.getState(),
                null,
                null,
                null,
                "nominatim-reverse"
        );
    }
}
