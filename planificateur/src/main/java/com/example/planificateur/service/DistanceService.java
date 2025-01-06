package com.example.planificateur.service;

/**
 * Permet de calculer la distance (en km) entre deux points (lat/long).
 */
public interface DistanceService {
    double computeDistance(GeocodingService.Coordinates c1, GeocodingService.Coordinates c2);
}
