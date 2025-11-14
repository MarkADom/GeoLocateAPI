package com.synchlabs.geolocateapi.application.dto;

import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeoLocationResponse {
    private String ip;
    private String city;
    private String region;
    private String country;
    private double latitude;
    private double longitude;
    private String timezone;
    private String org;
    private boolean cached;

    public static GeoLocationResponse from(GeoLocationData data) {
        return GeoLocationResponse.builder()
                .ip(data.ip())
                .city(data.city())
                .region(data.region())
                .country(data.country())
                .latitude(data.latitude())
                .longitude(data.longitude())
                .timezone(data.timezone())
                .org(data.org())
                .cached(false) // future: implement caching
                .build();
    }
}
