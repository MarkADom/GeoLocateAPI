package com.synchlabs.geolocateapi.infrastructure.client.search;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.search.dto.SearchGeoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CitySearchClient {

    private final RestTemplate restTemplate;

    @Value("${external.providers.open-meteo.search-url}")
    private String searchUrl;

    public CitySearchClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoLocationData findByCity(String city) {

        String url = searchUrl + "?name=" + city;

        log.info("Calling city search provider for '{}'", city);

        SearchGeoResponse response;

        try {
            response = restTemplate.getForObject(url, SearchGeoResponse.class);
        } catch (Exception ex) {
            log.error("City search provider failed for '{}': {}", city, ex.getMessage());
            throw new ExternalServiceException("Failed to search city: " + city);
        }

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            log.warn("City '{}' not found in provider", city);
            throw new ExternalServiceException("City not found: " + city);
        }

        var result = response.getResults().get(0);

        return new GeoLocationData(
                result.getLatitude(),
                result.getLongitude(),
                result.getCountry(),
                result.getName(),
                null,                 // region
                result.getTimezone(),
                null,                 // org
                null,                 // ip
                "open-meteo-search"
        );
    }
}
