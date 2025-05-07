package com.ubuntu.ubuntu_app.service.geo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ubuntu.ubuntu_app.infra.errors.GeocodeErrorException;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;

@Component
public class GeoLocationService {

    @Value("${nominatim.reverse}")
    private String nominatimUrl;

    @Cacheable(value = "locationCache", key = "#latitude + ',' + #longitude")
    public ResponseEntity<?> obtainUserLocation(double latitude, double longitude) {
        var obtainedLocation = getLocation(latitude, longitude);
        if (obtainedLocation == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ResponseMap.createResponse("Location", obtainedLocation));
    }

    private LocationDTO getLocation(double latitude, double longitude) {
        String url = UriComponentsBuilder.fromUriString(nominatimUrl)
                .queryParam("format", "jsonv2")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.getForObject(url, JsonDTO.class);
        if (response.error() != null && response.error().equals("Unable to geocode")) {
                throw new GeocodeErrorException("There was a problem with the response");
            }
        
        return response.address();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record JsonDTO(LocationDTO address, String error) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LocationDTO(String country, @JsonAlias("state") String province) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Nominatim(double lat, double lon) {
    }
}
