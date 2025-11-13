package com.synchlabs.geolocateapi.presentation.controller;

import com.synchlabs.geolocateapi.application.dto.GeoLocationResponse;
import com.synchlabs.geolocateapi.application.port.in.GeoQueryUseCase;
import com.synchlabs.geolocateapi.presentation.validation.ValidIp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@Validated
@RestController
@RequestMapping("/api/v1/geo")
public class GeoController {

    private final GeoQueryUseCase geoUseCase;

    public GeoController(GeoQueryUseCase geoUseCase) {
        this.geoUseCase = geoUseCase;
    }

    @GetMapping("/ip/{ip}")
    public ResponseEntity<GeoLocationResponse> getByIp(
            @ValidIp
            @PathVariable String ip
    ) {
        var result = GeoLocationResponse.from(geoUseCase.findByIp(ip));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<GeoLocationResponse> getByCity(
            @PathVariable String city
    ) {
        var result = GeoLocationResponse.from(geoUseCase.findByCity(city));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/coordinates")
    public ResponseEntity<GeoLocationResponse> getByCoordinates(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        var result = GeoLocationResponse.from(geoUseCase.findByCoordinates(lat, lon));
        return ResponseEntity.ok(result);
    }
}
