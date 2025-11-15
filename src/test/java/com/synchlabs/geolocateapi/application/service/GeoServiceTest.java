package com.synchlabs.geolocateapi.application.service;

import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.application.port.in.GeoQueryUseCase;
import com.synchlabs.geolocateapi.application.port.out.GeoProviderPort;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeoServiceTest {

    private GeoProviderPort provider;
    private GeoQueryUseCase service;

    @BeforeEach
    void setup() {
        provider = mock(GeoProviderPort.class);
        service = new GeoService(provider);
    }

    @Test
    void shouldDelegateFindByIpToProvider() {
        GeoLocationData data = dummy();
        when(provider.findByIp("8.8.8.8")).thenReturn(data);

        GeoLocationData result = service.findByIp("8.8.8.8");

        assertEquals(data, result);
        verify(provider, times(1)).findByIp("8.8.8.8");
    }

    @Test
    void shouldDelegateFindByCoordinatesToProvider() {
        GeoLocationData data = dummy();
        when(provider.findByCoordinates(10.0, 20.0)).thenReturn(data);

        GeoLocationData result = service.findByCoordinates(10.0, 20.0);

        assertEquals(data, result);
        verify(provider).findByCoordinates(10.0, 20.0);
    }

    @Test
    void shouldDelegateFindByCityToProvider() {
        GeoLocationData data = dummy();
        when(provider.findByCity("Lisbon")).thenReturn(data);

        GeoLocationData result = service.findByCity("Lisbon");

        assertEquals(data, result);
        verify(provider).findByCity("Lisbon");
    }

    @Test
    void shouldPropagateProviderExceptions() {
        when(provider.findByIp("1.1.1.1"))
                .thenThrow(new ExternalServiceException("provider failure"));

        assertThrows(ExternalServiceException.class,
                () -> service.findByIp("1.1.1.1"));
    }

    // small helper to avoid repetition
    private GeoLocationData dummy() {
        return new GeoLocationData(0, 0, "PT", "Lisbon", "Lisbon District",
                "Europe/Lisbon", "ISP", "0.0.0.0", "test");
    }
}
