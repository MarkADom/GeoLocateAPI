package com.synchlabs.geolocateapi.infrastructure.client.reverse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * DTO representing the JSON response returned by the Nominatim reverse
 * geocoding endpoint.
 *
 * This class mirrors the external API format and should never be used
 * outside the infrastructure layer.
 *
 * The application and domain layers interact only with mapped domain models.
 */
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
