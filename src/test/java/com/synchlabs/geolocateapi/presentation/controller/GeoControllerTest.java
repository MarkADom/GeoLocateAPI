package com.synchlabs.geolocateapi.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synchlabs.geolocateapi.application.exception.ExternalServiceException;
import com.synchlabs.geolocateapi.application.port.in.GeoQueryUseCase;
import com.synchlabs.geolocateapi.domain.model.GeoLocationData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc-based test for {@link GeoController}.
 *
 * Validates:
 * - request routing and HTTP endpoints
 * - input validation behavior
 * - correct HTTP response formatting
 * - error handling at the presentation layer
 *
 * The application layer is mocked to isolate controller logic.
 *
 * Type: Controller Test (MockMvc).
 */
@WebMvcTest(controllers = GeoController.class)
class GeoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoQueryUseCase service;

    @Autowired
    private ObjectMapper objectMapper;

    private GeoLocationData dummy() {
        return new GeoLocationData(
                38.7,
                -9.1,
                "Portugal",
                "Lisbon",
                "Lisbon District",
                "Europe/Lisbon",
                "ISP",
                "8.8.8.8",
                "test"
        );
    }

    // ========= IP LOOKUP =========

    @Test
    void shouldReturnGeoDataByIp() throws Exception {
        when(service.findByIp("8.8.8.8")).thenReturn(dummy());

        mockMvc.perform(get("/api/v1/geo/ip/8.8.8.8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Lisbon"))
                .andExpect(jsonPath("$.country").value("Portugal"))
                .andExpect(jsonPath("$.latitude").value(38.7));
    }

    @Test
    void shouldRejectInvalidIp() throws Exception {
        mockMvc.perform(get("/api/v1/geo/ip/999.999.999.999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldHandleProviderErrorForIp() throws Exception {
        when(service.findByIp("8.8.4.4"))
                .thenThrow(new ExternalServiceException("upstream failure"));

        mockMvc.perform(get("/api/v1/geo/ip/8.8.4.4"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").value("Bad Gateway"))
                .andExpect(jsonPath("$.message").value("upstream failure"));
    }

    // ========= CITY LOOKUP =========

    @Test
    void shouldReturnGeoDataByCity() throws Exception {
        when(service.findByCity("Lisbon")).thenReturn(dummy());

        mockMvc.perform(get("/api/v1/geo/city/Lisbon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Lisbon"))
                .andExpect(jsonPath("$.country").value("Portugal"));
    }

    @Test
    void shouldHandleCityProviderError() throws Exception {
        when(service.findByCity("Nowhere"))
                .thenThrow(new ExternalServiceException("City not found"));

        mockMvc.perform(get("/api/v1/geo/city/Nowhere"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message").value("City not found"));
    }

    // ========= COORDINATES LOOKUP =========

    @Test
    void shouldReturnGeoDataByCoordinates() throws Exception {
        when(service.findByCoordinates(10.0, 20.0)).thenReturn(dummy());

        mockMvc.perform(get("/api/v1/geo/coordinates?lat=10.0&lon=20.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Lisbon"))
                .andExpect(jsonPath("$.latitude").value(38.7));
    }

    @Test
    void shouldHandleProviderErrorForCoordinates() throws Exception {
        when(service.findByCoordinates(1, 2))
                .thenThrow(new ExternalServiceException("Coords provider failure"));

        mockMvc.perform(get("/api/v1/geo/coordinates?lat=1&lon=2"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message").value("Coords provider failure"));
    }
}
