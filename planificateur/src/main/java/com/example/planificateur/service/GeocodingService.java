package com.example.planificateur.service;

import com.example.planificateur.service.model.Coordinates;

public interface GeocodingService {
    Coordinates geocodeAddress(String address) throws GeocodingException;
}