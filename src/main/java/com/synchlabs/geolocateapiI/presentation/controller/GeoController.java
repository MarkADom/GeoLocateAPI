package com.synchlabs.geolocateapiI.presentation.controller;


import com.synchlabs.geolocateapiI.application.dto.GeoLocationResponse;
import com.synchlabs.geolocateapiI.application.service.GeoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/geo")
@RequiredArgsConstructor
@Slf4j
public class GeoController {

    private final GeoService geoService;

    @GetMapping("/{ip}")
    public GeoLocationResponse getByIp(@PathVariable String ip) {
        log.info("Received request for IP: {}", ip);
        return geoService.getLocation(ip);
    }
}
