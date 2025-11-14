package com.synchlabs.geolocateapi.infrastructure.client.search;

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

class CitySearchClientTest {

    private WireMockServer wireMock;
    private CitySearchClient client;

    @BeforeEach
    void setup() {
        wireMock = new WireMockServer(8091);
        wireMock.start();
        configureFor("localhost", 8091);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        client = new CitySearchClient(restTemplate,
                "http://localhost:8091/search"
        );
    }

    @AfterEach
    void teardown() {
        wireMock.stop();
    }

    @Test
    void shouldParseValidCitySearchResponse() {

        stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("name", equalTo("Lisbon"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "results": [
                                {
                                  "name": "Lisbon",
                                  "country": "Portugal",
                                  "latitude": 38.7167,
                                  "longitude": -9.1333,
                                  "timezone": "Europe/Lisbon"
                                }
                              ]
                            }
                        """)));

        GeoLocationData data = client.findByCity("Lisbon");

        assertEquals("Lisbon", data.city());
        assertEquals("Portugal", data.country());
        assertEquals(38.7167, data.latitude());
        assertEquals(-9.1333, data.longitude());
        assertEquals("Europe/Lisbon", data.timezone());

    }

    @Test
    void shouldThrowExceptionWhenNoResults() {

        stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("name", equalTo("UnknownCity"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "results": []
                            }
                        """)));

        assertThrows(ExternalServiceException.class,
                () -> client.findByCity("UnknownCity"));
    }

    @Test
    void shouldThrowExceptionOnProviderError() {

        stubFor(get(urlPathEqualTo("/search"))
                .withQueryParam("name", equalTo("Porto"))
                .willReturn(aResponse().withStatus(500)));

        assertThrows(ExternalServiceException.class,
                () -> client.findByCity("Porto"));
    }
}
