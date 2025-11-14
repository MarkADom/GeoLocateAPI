package com.synchlabs.geolocateapi.infrastructure.client.ip;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.ip.dto.IpApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class IpGeoClient {

    private final RestTemplate restTemplate;

    @Value("${external.providers.ip-api.url}")
    private String ipApiUrl;

    public IpGeoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeoLocationData findByIp(String ip) {

        long start = System.currentTimeMillis();
        String url = ipApiUrl + "/" + ip;

        log.info("Calling IP provider for {}", ip);

        IpApiResponse response;

        try {
            response = restTemplate.getForObject(url, IpApiResponse.class);
        } catch (Exception ex) {
            log.error("IP provider failed for {}: {}", ip, ex.getMessage());
            throw new ExternalServiceException("Failed to fetch geolocation for IP: " + ip);
        }

        long duration = System.currentTimeMillis() - start;
        log.info("IP provider responded in {} ms for {}", duration, ip);

        if (response == null || !"success".equalsIgnoreCase(response.getStatus())) {
            log.error("Invalid provider response for {}", ip);
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
                response.getQuery(),
                "ip-api"
        );
    }
}
