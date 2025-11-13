package com.synchlabs.geolocateapi.infrastructure.client;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.application.port.out.GeoProviderPort;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.dto.IpApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeoApiClient implements GeoProviderPort {

    private final RestTemplate restTemplate;

    @Value("${external.geo.base-url}")
    private String baseUrl;

    @Value("${external.geo.provider}")
    private String provider;


    public GeoApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GeoLocationData findByIp(String ip)  {
        String url = baseUrl + "/json/" + ip;

        IpApiResponse response = restTemplate.getForObject(url, IpApiResponse.class);

        if (response == null || !"success".equalsIgnoreCase(response.getStatus())) {
            throw new ExternalServiceException("Failed to fetch geolocation for IP: " + ip);
        }
        return new GeoLocationData(
                response.getLat(),
                response.getLon(),
                response.getCountry(),
                response.getCity(),
                response.getRegionName(),
                response.getTimezone(),
                response.getIsp(),
                response.getQuery(),    // ip
                "ip-api.com"            // source
        );
    }

    @Override
    public GeoLocationData findByCoordinates(double lat, double lon) {
        throw new UnsupportedOperationException("Reverse geocoding not implemented yet");
    }

    @Override
    public GeoLocationData findByCity(String city) {
        throw new UnsupportedOperationException("City lookup not implemented yet");
    }
}
