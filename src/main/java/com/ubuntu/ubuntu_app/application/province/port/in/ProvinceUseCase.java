package com.ubuntu.ubuntu_app.application.province.port.in;

import java.util.List;

import com.ubuntu.ubuntu_app.application.province.api.ProvinceResponse;

public interface ProvinceUseCase {

    List<ProvinceResponse> findProvincesByCountry(String countryName);
}
