package com.ubuntu.ubuntu_app.application.country.port.in;

import java.util.List;

import com.ubuntu.ubuntu_app.application.country.api.CountryResponse;

public interface CountryUseCase {

    List<CountryResponse> findAll();
}
