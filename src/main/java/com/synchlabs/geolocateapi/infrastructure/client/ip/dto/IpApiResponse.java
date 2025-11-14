package com.synchlabs.geolocateapi.infrastructure.client.ip.dto;

import lombok.Data;

@Data
public class IpApiResponse {
    private String status;
    private String country;
    private String regionName;
    private String city;
    private double lat;
    private double lon;
    private String timezone;
    private String isp;
    private String query;   // ip
}
