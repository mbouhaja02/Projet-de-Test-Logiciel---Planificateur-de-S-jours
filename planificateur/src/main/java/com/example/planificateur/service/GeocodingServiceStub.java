package com.example.planificateur.service;

import com.example.planificateur.service.GeocodingException;
import org.springframework.stereotype.Service;

/**
 * Stub qui renvoie des coordonnées fictives. 
 * Dans la vraie vie, on appellerait un service de geocoding (ex: geocode.maps).
 */
@Service
public class GeocodingServiceStub implements GeocodingService {
    @Override
    public Coordinates geocodeAddress(String address) throws GeocodingException {
        // On renvoie un point fictif en fonction du hash du string, juste pour la démo
        int hash = Math.abs(address.hashCode());
        double lat = (hash % 90) - 45;      // un lat "bidon"
        double lon = (hash % 180) - 90;     // un lon "bidon"
        return new Coordinates(lat, lon);
    }
}
