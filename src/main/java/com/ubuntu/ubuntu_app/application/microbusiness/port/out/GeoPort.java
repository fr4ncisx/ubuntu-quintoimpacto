package com.ubuntu.ubuntu_app.application.microbusiness.port.out;

import com.ubuntu.ubuntu_app.shared.geo.GeoLocationService.Nominatim;

public interface GeoPort {

    double calculate(double latOrigin, double lonOrigin, double latTo, double lonTo);

    Nominatim getCoordinatesByName(String province, String city, String country);
}
