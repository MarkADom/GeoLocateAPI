package com.synchlabs.geolocateapi.infrastructure.client.search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

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
