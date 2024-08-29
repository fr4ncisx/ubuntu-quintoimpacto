package com.ubuntu.ubuntu_app.service.geo;

import java.text.DecimalFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ubuntu.ubuntu_app.infra.errors.GeocodeErrorException;
import com.ubuntu.ubuntu_app.service.geo.GeoLocationService.Nominatim;

@Component
public class GeoDistanceService {

    @Value("${nominatim.search}")
    private String nominatimUrl;

    public double calculate(double latOrigin, double lonOrigin, double latTo, double lonTo) {
        double TOTAL_RADIUS = 6371.0;
        double latDistance = Math.toRadians(latTo - latOrigin);
        double lonDistance = Math.toRadians(lonTo - lonOrigin);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(latOrigin))
                * Math.cos(Math.toRadians(latTo)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var total = TOTAL_RADIUS * c;
        return cleanDecimalFormat(total, "#.##");
    }

    public Nominatim getCoordinatesByName(String province, String city, String country) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder
            .fromHttpUrl(nominatimUrl)
            .queryParam("city", city)
            .queryParam("state", province)
            .queryParam("country", country)
            .queryParam("format", "json")
            .queryParam("limit", 1)
            .build(false)
            .toUriString();
            System.out.println(url);
        Nominatim[] response = restTemplate.getForObject(url, Nominatim[].class);
        if (response == null || response.length == 0) {
            throw new GeocodeErrorException("Unable to return coordinates");
        }
        return response[0];

    }

    private double cleanDecimalFormat(double d, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return Double.parseDouble(df.format(d).replace(",", "."));
    }

    // private String encode(String input) {
    //     String normalizedCityName = Normalizer.normalize(input, Normalizer.Form.NFD);
    //         normalizedCityName = normalizedCityName.replaceAll("\\p{M}", "");
    //         return normalizedCityName.replace(" ", "+");
    // }
}
