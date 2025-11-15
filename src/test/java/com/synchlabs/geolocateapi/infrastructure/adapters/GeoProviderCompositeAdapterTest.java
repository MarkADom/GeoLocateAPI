package com.synchlabs.geolocateapi.infrastructure.adapters;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import com.synchlabs.geolocateapi.infrastructure.client.ip.IpGeoClient;
import com.synchlabs.geolocateapi.infrastructure.client.reverse.ReverseGeoClient;
import com.synchlabs.geolocateapi.infrastructure.client.search.CitySearchClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeoProviderCompositeAdapterTest {

    private IpGeoClient ipClient;
    private ReverseGeoClient reverseClient;
    private CitySearchClient cityClient;

    private GeoProviderCompositeAdapter adapter;

    @BeforeEach
    void setup() {
        ipClient = mock(IpGeoClient.class);
        reverseClient = mock(ReverseGeoClient.class);
        cityClient = mock(CitySearchClient.class);

        adapter = new GeoProviderCompositeAdapter(ipClient, reverseClient, cityClient);
    }

    // ========= IP LOOKUP =========

    @Test
    void shouldReturnResultFromIpProvider() {
        GeoLocationData mockData = new GeoLocationData(
                10.0, 20.0, "Portugal", "Lisbon",
                "Lisbon District", "Europe/Lisbon",
                "PT ISP", "1.1.1.1", "ip-api"
        );

        when(ipClient.findByIp("1.1.1.1")).thenReturn(mockData);

        GeoLocationData result = adapter.findByIp("1.1.1.1");

        assertEquals("Lisbon", result.city());
        verify(ipClient, times(1)).findByIp("1.1.1.1");
    }

    @Test
    void shouldThrowIfIpProviderFails() {
        when(ipClient.findByIp("8.8.8.8"))
                .thenThrow(new ExternalServiceException("upstream"));

        assertThrows(ExternalServiceException.class,
                () -> adapter.findByIp("8.8.8.8"));

        verify(ipClient).findByIp("8.8.8.8");
    }

    // ========= CITY LOOKUP =========

    @Test
    void shouldReturnResultFromCityProvider() {
        GeoLocationData mockData = new GeoLocationData(
                38.7, -9.1, "Portugal", "Lisbon",
                "Lisbon District", "Europe/Lisbon",
                null, null, "open-meteo-search"
        );

        when(cityClient.findByCity("Lisbon")).thenReturn(mockData);

        GeoLocationData result = adapter.findByCity("Lisbon");

        assertEquals("Lisbon", result.city());
        verify(cityClient, times(1)).findByCity("Lisbon");
    }

    @Test
    void shouldThrowIfCityProviderFails() {
        when(cityClient.findByCity("Nowhere"))
                .thenThrow(new ExternalServiceException("City not found"));

        assertThrows(ExternalServiceException.class,
                () -> adapter.findByCity("Nowhere"));
    }

    // ========= COORDINATES LOOKUP =========

    @Test
    void shouldReturnResultFromReverseProvider() {
        GeoLocationData mockData = new GeoLocationData(
                40.0, -8.0, "Portugal", "Coimbra",
                "Coimbra District", "Europe/Lisbon",
                null, null, "nominatim-reverse"
        );

        when(reverseClient.findByCoordinates(40.0, -8.0)).thenReturn(mockData);

        GeoLocationData result = adapter.findByCoordinates(40.0, -8.0);

        assertEquals("Coimbra", result.city());
        verify(reverseClient).findByCoordinates(40.0, -8.0);
    }

    @Test
    void shouldThrowIfReverseProviderFails() {
        when(reverseClient.findByCoordinates(10.0, 10.0))
                .thenThrow(new ExternalServiceException("No address"));

        assertThrows(ExternalServiceException.class,
                () -> adapter.findByCoordinates(10.0, 10.0));
    }

}
