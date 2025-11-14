package com.synchlabs.geolocateapi.infrastructure.client.reverse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReverseGeoResponse {

    private String lat;
    private String lon;
    private Address address;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        private String city;
        private String town;
        private String village;
        private String state;
        private String country;
    }
}
