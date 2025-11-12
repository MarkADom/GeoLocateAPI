package com.synchlabs.geolocateapi.application.service;

import com.synchlabs.geolocateapi.application.dto.GeoLocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class GeoService {

    private final RestClient restClient;

    @Value("${external.geo.base-url}")
    private String baseUrl;

    public GeoService() {
        this.restClient = RestClient.create();
    }

    public GeoLocationResponse getLocation(String ip) {
        String url = String.format("%s/%s/json", baseUrl, ip);
        log.info("Fetching location for IP: {}", ip);

        try {
            var response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(IpApiResponse.class);

            if (response == null) {
                throw new IllegalStateException("Empty response from external API");
            }

            return GeoLocationResponse.builder()
                    .ip(ip)
                    .city(response.city())
                    .region(response.region())
                    .country(response.country_name())
                    .latitude(response.latitude())
                    .longitude(response.longitude())
                    .timezone(response.timezone())
                    .org(response.org())
                    .cached(false)
                    .build();

        } catch (RestClientException e) {
            log.error("Error calling external API: {}", e.getMessage());
            throw new RuntimeException("External API error", e);
        }
    }

    private record IpApiResponse(
            String city,
            String region,
            String country_name,
            double latitude,
            double longitude,
            String timezone,
            String org
    ) {}
}
