package com.synchlabs.geolocateapi.infrastructure.client.reverse;

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

class ReverseGeoClientTest {

    private WireMockServer wireMock;
    private ReverseGeoClient client;

    @BeforeEach
    void setup() {
        wireMock = new WireMockServer(8090);
        wireMock.start();
        configureFor("localhost", 8090);

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        client = new ReverseGeoClient(restTemplate,
                "http://localhost:8090/reverse"
        );
    }

    @AfterEach
    void teardown() {
        wireMock.stop();
    }

    @Test
    void shouldParseValidReverseGeoResponse() {

        stubFor(get(urlPathEqualTo("/reverse"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "lat": "40.7128",
                              "lon": "-74.0060",
                              "address": {
                                "city": "New York",
                                "state": "New York",
                                "country": "USA"
                              }
                            }
                        """)));

        GeoLocationData data = client.findByCoordinates(40.7128, -74.0060);

        assertEquals("USA", data.country());
        assertEquals("New York", data.city());
        assertEquals(40.7128, data.latitude());
        assertEquals(-74.0060, data.longitude());
    }

    @Test
    void shouldFallbackToTownIfCityMissing() {

        stubFor(get(urlPathEqualTo("/reverse"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "lat": "41.15",
                              "lon": "-8.61",
                              "address": {
                                "town": "Porto",
                                "state": "Porto",
                                "country": "Portugal"
                              }
                            }
                        """)));

        GeoLocationData data = client.findByCoordinates(41.15, -8.61);
        assertEquals("Porto", data.city());
    }

    @Test
    void shouldFallbackToVillageIfCityAndTownMissing() {

        stubFor(get(urlPathEqualTo("/reverse"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                        {
                          "lat": "41.10",
                          "lon": "-8.55",
                          "address": {
                            "village": "Fânzeres",
                            "state": "Porto",
                            "country": "Portugal"
                          }
                        }
                    """)));

        GeoLocationData data = client.findByCoordinates(41.10, -8.55);

        assertEquals("Fânzeres", data.city());
        assertEquals("Portugal", data.country());
        assertEquals("Porto", data.region());
    }


    @Test
    void shouldThrowExceptionOnMissingAddress() {

        stubFor(get(urlPathEqualTo("/reverse"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                              "lat": "41.0",
                              "lon": "-8.0"
                            }
                        """)));

        assertThrows(ExternalServiceException.class,
                () -> client.findByCoordinates(41.0, -8.0));
    }

    @Test
    void shouldThrowExceptionOnProviderError() {

        stubFor(get(urlPathEqualTo("/reverse"))
                .willReturn(aResponse().withStatus(500)));

        assertThrows(ExternalServiceException.class,
                () -> client.findByCoordinates(41.0, -8.0));
    }
}
