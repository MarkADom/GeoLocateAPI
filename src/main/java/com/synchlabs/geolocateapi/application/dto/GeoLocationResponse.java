package com.synchlabs.geolocateapi.application.dto;

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
}
