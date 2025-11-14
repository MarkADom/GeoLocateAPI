package com.synchlabs.geolocateapi.infrastructure.client.reverse.dto;

import lombok.Data;

@Data
public class ReverseGeoResponse {

    private String lat;
    private String lon;
    private Address address;

    @Data
    public static class Address {
        private String city;
        private String town;
        private String village;
        private String state;
        private String country;
    }
}
