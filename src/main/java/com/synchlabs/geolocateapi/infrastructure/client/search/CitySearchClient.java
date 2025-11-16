package com.synchlabs.geolocateapi.infrastructure.client.search;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.search.dto.SearchGeoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Infrastructure adapter for city name search operations.
 *
 * Calls the Open-Meteo Geocoding API to retrieve coordinates and location
 * metadata based on a city name query.
 *
 * Responsibilities:
 * - HTTP interaction with the provider
 * - Mapping provider responses to {@link SearchGeoResponse}
 * - Escaping all provider-specific data models from upper layers
 */
@Slf4j
@Component
public class CitySearchClient {

    private final RestTemplate restTemplate;

    @Value("${external.providers.open-meteo.search-url}")
    private String searchUrl;

    public CitySearchClient(
            RestTemplate restTemplate,
            @Value("${external.providers.open-meteo.search-url}")
            String searchUrl
    ) {
        this.restTemplate = restTemplate;
        this.searchUrl = searchUrl;
    }

    public GeoLocationData findByCity(String city) {

        String url = UriComponentsBuilder.fromHttpUrl(searchUrl)
                .queryParam("name", city)
                .queryParam("count", 5)
                .queryParam("format", "json")
                .toUriString();

        log.info("Calling Open-Meteo city search for '{}'", city);
        log.debug("City search URL: {}", url);

        SearchGeoResponse response;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "GeoLocateAPI/1.0");
            headers.set("Accept", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<SearchGeoResponse> result = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SearchGeoResponse.class
            );

            response = result.getBody();

        } catch (Exception ex) {
            log.error("HTTP error calling Open-Meteo for '{}': {}", city, ex.getMessage(), ex);
            throw new ExternalServiceException("City search provider failed for city: " + city, ex);
        }

        if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
            log.warn("No results for '{}' in Open-Meteo", city);
            throw new ExternalServiceException( "No results returned by Open-Meteo for city: " + city);
        }

        // Choose best match (exact match if possible)
        var result = selectBestResult(response, city);

        return new GeoLocationData(
                result.getLatitude(),
                result.getLongitude(),
                result.getCountry(),
                result.getName(),
                null,
                result.getTimezone(),
                null,
                null,
                "open-meteo-search"
        );

    }

    private SearchGeoResponse.Result selectBestResult(SearchGeoResponse response, String city) {
        return response.getResults().stream()
                .filter(r -> r.getName() != null && r.getName().equalsIgnoreCase(city))
                .findFirst()
                .orElse(response.getResults().get(0));
    }

}
