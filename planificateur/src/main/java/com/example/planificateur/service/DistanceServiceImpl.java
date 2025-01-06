package com.example.planificateur.service;

import org.springframework.stereotype.Service;

/**
 * Implémentation via la formule de Haversine (à vol d'oiseau)
 */
@Service
public class DistanceServiceImpl implements DistanceService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public double computeDistance(GeocodingService.Coordinates c1, GeocodingService.Coordinates c2) {
        double lat1 = Math.toRadians(c1.getLatitude());
        double lon1 = Math.toRadians(c1.getLongitude());
        double lat2 = Math.toRadians(c2.getLatitude());
        double lon2 = Math.toRadians(c2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
