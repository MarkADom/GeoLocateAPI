package com.synchlabs.geolocateapiI.application.service;

import com.synchlabs.geolocateapiI.application.dto.GeoLocationResponse;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
@Slf4j
public class GeoService {

    private final WebClient webClient;

    @Value("${external.geo.base-url}")
    private String baseUrl;

    public GeoService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public GeoLocationResponse getLocation(String ip) {
        String url = String.format("%s/%s/json", baseUrl, ip);
        log.info("Fetching location for IP: {}", ip);

        try {
            var response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(MapResponse.class)
                    .block();

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

        } catch (WebClientResponseException e) {
            log.error("Error form external API: {}", e.getResponseBodyAsString());
            throw new RuntimeException("External API error", e);
        }
    }

    private record MapResponse(
            String city,
            String region,
            String country_name,
            double latitude,
            double longitude,
            String timezone,
            String org
    ) {}
}
