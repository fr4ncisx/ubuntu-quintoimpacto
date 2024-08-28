package com.ubuntu.ubuntu_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ubuntu.ubuntu_app.service.geo.GeoLocationService;

import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/api")
public class GeoController {
    
    @Autowired
    GeoLocationService geoLocationService;

    @PermitAll
    @GetMapping("/geo")
    public ResponseEntity<?> getGeo(@RequestParam double lat, @RequestParam double lon){
        return geoLocationService.obtainUserLocation(lat, lon);
    }
}
