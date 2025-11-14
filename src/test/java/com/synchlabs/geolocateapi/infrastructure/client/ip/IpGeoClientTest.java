package com.synchlabs.geolocateapi.infrastructure.client.ip;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class IpGeoClientTest {

    private WireMockServer wireMock;
    private IpGeoClient client;

    @BeforeEach
    void setup() {
        wireMock = new WireMockServer(8089);
        wireMock.start();
        configureFor("localhost", 8089);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        // we use the builder that receives the URL in IpGeoClient
        client = new IpGeoClient(restTemplate, "http://localhost:8089/json");
    }

    @AfterEach
    void teardown() {
        if (wireMock != null) {
            wireMock.stop();
        }
    }

    @Test
    void shouldParseValidResponse() {
        stubFor(get(urlEqualTo("/json/8.8.8.8"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "status": "success",
                                  "country": "United States",
                                  "regionName": "Virginia",
                                  "city": "Ashburn",
                                  "lat": 39.03,
                                  "lon": -77.5,
                                  "timezone": "America/New_York",
                                  "isp": "Google LLC",
                                  "query": "8.8.8.8"
                                }
                                """)));

        GeoLocationData result = client.findByIp("8.8.8.8");

        assertEquals("United States", result.country());
        assertEquals("Ashburn", result.city());
        assertEquals(39.03, result.latitude());
        assertEquals(-77.5, result.longitude());
        assertEquals("8.8.8.8", result.ip());
        assertEquals("Virginia", result.region());

    }

    @Test
    void shouldThrowExceptionOnErrorResponse() {
        stubFor(get(urlEqualTo("/json/1.1.1.1"))
                .willReturn(aResponse().withStatus(500)));

        assertThrows(ExternalServiceException.class,
                () -> client.findByIp("1.1.1.1"));
    }

    @Test
    void shouldThrowExceptionOnInvalidJson() {
        stubFor(get(urlEqualTo("/json/9.9.9.9"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{ invalid-json }")));

        assertThrows(ExternalServiceException.class,
                () -> client.findByIp("9.9.9.9"));
    }
}
