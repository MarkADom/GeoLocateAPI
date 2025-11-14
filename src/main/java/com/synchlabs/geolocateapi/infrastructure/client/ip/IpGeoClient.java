package com.synchlabs.geolocateapi.infrastructure.client.ip;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.ip.dto.IpApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class IpGeoClient {

    private final RestTemplate restTemplate;


    private String ipApiUrl;

    public IpGeoClient(
            RestTemplate restTemplate,
            @Value("${external.providers.ip-api.url}")
            String ipApiUrl
    ) {
        this.restTemplate = restTemplate;
        this.ipApiUrl = ipApiUrl;
    }

    public GeoLocationData findByIp(String ip) {

        long start = System.currentTimeMillis();
        String url = ipApiUrl + "/" + ip;

        log.info("Calling IP provider [ip-api] for {}", ip);

        IpApiResponse response;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.USER_AGENT, "GeoLocateAPI/1.0");
            headers.set(HttpHeaders.ACCEPT, "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<IpApiResponse> result = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    IpApiResponse.class
            );

            response = result.getBody();

        } catch (Exception ex) {
            log.error("HTTP error calling ip-api for {}: {}", ip, ex.getMessage(), ex);
            throw new ExternalServiceException("" +
                    "Failed to fetch geolocation for IP: " + ip,
                    ex
            );
        }

        long duration = System.currentTimeMillis() - start;
        log.info("ip-api responded in {} ms for {}", duration, ip);

        // Validate response
        if (response == null) {
            log.error("ip-api returned NULL body for {}", ip);
            throw new ExternalServiceException("" +
                    "Null response body from ip-api for IP: " + ip
            );
        }

        if (!"success".equalsIgnoreCase(response.getStatus())) {
            log.error("ip-api returned non-success status='{}' for {} (city={})",
                    response.getStatus(),
                    ip,
                    response.getCity()
            );
            throw new ExternalServiceException(
                    "ip-api returned  invalid status=" +
                            response.getStatus() +
                            "' for IP: " +
                            ip
            );
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
