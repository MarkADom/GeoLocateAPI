package com.synchlabs.geolocateapi.infrastructure.client.reverse;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.reverse.dto.ReverseGeoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class ReverseGeoClient {

    private final RestTemplate restTemplate;

    @Value("${external.providers.nominatim.reverse-url}")
    private String reverseUrl;

    public ReverseGeoClient(
            RestTemplate restTemplate,
            @Value("${external.providers.nominatim.reverse-url}")
            String reverseUrl
    ) {
        this.restTemplate = restTemplate;
        this.reverseUrl = reverseUrl;
    }

    public GeoLocationData findByCoordinates(double lat, double lon) {

        long start = System.currentTimeMillis();

        String url = UriComponentsBuilder.fromHttpUrl(reverseUrl)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("format", "json")
                .queryParam("addressdetails", 1)
                .queryParam("zoom", 12)
                .toUriString();

        log.info("Calling Nominatim reverse provider for {}, {}", lat, lon);

        ReverseGeoResponse response;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "GeoLocateAPI/1.0");
            headers.set("Accept", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ReverseGeoResponse> result = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    ReverseGeoResponse.class
            );

            response = result.getBody();

        } catch (Exception ex) {
            log.error("HTTP error calling Nominatim for {}, {}: {}",
                    lat, lon, ex.getMessage(), ex);
            throw new ExternalServiceException(
                    "Failed to call reverse geocoding provider (Nominatim) for coordinates: " +
                            lat + ", " + lon,
                    ex
            );
        }

        long duration = System.currentTimeMillis() - start;
        log.info("Nominatim responded in {} ms for {}, {}", duration, lat, lon);

        if (response == null || response.getAddress() == null) {
            log.error("Nominatim returned null/invalid address for {}, {}", lat, lon);
            throw new ExternalServiceException(
                    "Nominatim returned no valid address for: " +
                            lat + ", " + lon
            );
        }

        var addr = response.getAddress();

        String city = extractCity(addr);

        return new GeoLocationData(
                safeDouble(response.getLat()),
                safeDouble(response.getLon()),
                addr.getCountry(),
                city,
                addr.getState(),
                null,
                null,
                null,
                "nominatim-reverse"
        );
    }

    private double safeDouble(String value) {
        try {
            return value == null ? 0.0 : Double.parseDouble(value);
        } catch (Exception ex) {
            log.warn("Invalid numeric value '{}' from provider", value, ex);
            return 0.0;
        }
    }

    private String extractCity(ReverseGeoResponse.Address addr) {
        if (addr.getCity() != null) return addr.getCity();
        if (addr.getTown() != null) return addr.getTown();
        if (addr.getVillage() != null) return addr.getVillage();
        return null;
    }
}
