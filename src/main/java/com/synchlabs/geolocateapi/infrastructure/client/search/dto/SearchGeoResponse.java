package com.synchlabs.geolocateapi.infrastructure.client.search.dto;

import lombok.Data;
import java.util.List;

@Data
public class SearchGeoResponse {

    private List<Result> results;

    @Data
    public static class Result {
        private String name;      // city
        private String country;
        private double latitude;
        private double longitude;
        private String timezone;
    }
}
