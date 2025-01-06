package com.example.planificateur.service;

import com.example.planificateur.service.GeocodingException;

public interface GeocodingService {
    Coordinates geocodeAddress(String address) throws GeocodingException;

    class Coordinates {
        private double latitude;
        private double longitude;

        public Coordinates(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }
}
