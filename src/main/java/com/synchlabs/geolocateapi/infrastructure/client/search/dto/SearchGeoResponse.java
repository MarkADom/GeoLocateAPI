package com.synchlabs.geolocateapi.infrastructure.client.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

/**
 * DTO representing the response returned by the Open-Meteo geocoding API.
 *
 * This structure reflects the provider's raw JSON schema and should be used
 * exclusively inside the infrastructure layer.
 *
 * Mapping from this DTO to the domain model occurs within the adapter.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchGeoResponse {
    private List<Result> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String name;      // city
        private String country;
        private double latitude;
        private double longitude;
        private String timezone;
    }
}
