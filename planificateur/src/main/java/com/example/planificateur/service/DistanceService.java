package com.example.planificateur.service;

import com.example.planificateur.service.model.Coordinates;

/**
 * Permet de calculer la distance (en km) entre deux points (lat/long).
 */
public interface DistanceService {
    double computeDistance(Coordinates c1, Coordinates c2);
}
