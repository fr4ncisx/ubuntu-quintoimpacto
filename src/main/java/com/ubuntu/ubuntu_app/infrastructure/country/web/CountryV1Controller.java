package com.ubuntu.ubuntu_app.infrastructure.country.web;

import java.util.List;
import java.util.Map;

import com.ubuntu.ubuntu_app.application.country.port.in.CountryUseCase;
import com.ubuntu.ubuntu_app.application.province.port.in.ProvinceUseCase;
import com.ubuntu.ubuntu_app.application.country.api.CountryResponse;
import com.ubuntu.ubuntu_app.application.province.api.ProvinceResponse;
import com.ubuntu.ubuntu_app.shared.api.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CountryV1Controller {

    private final CountryUseCase countryService;
    private final ProvinceUseCase provinceService;

    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<CountryResponse>>> findAllCountries() {
        return ResponseEntity.ok(ApiResponse.ok(countryService.findAll()));
    }

    @GetMapping("/provinces")
    public ResponseEntity<ApiResponse<List<ProvinceResponse>>> findProvincesByCountry(
            @RequestParam String country) {
        return ResponseEntity.ok(ApiResponse.ok(provinceService.findProvincesByCountry(country)));
    }
}
