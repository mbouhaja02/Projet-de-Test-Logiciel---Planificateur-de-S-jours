package com.example.planificateur;

import com.example.planificateur.service.model.Coordinates;
import com.example.planificateur.service.DistanceService;
import com.example.planificateur.service.DistanceServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DistanceServiceImplTest {

    private DistanceService distanceService;

    @BeforeEach
    void setUp() {
        distanceService = new DistanceServiceImpl();
    }

    @Test
    void computeDistance_samePoint_returnsZero() {
        var point = new Coordinates(48.8566, 2.3522); // Paris

        double distance = distanceService.computeDistance(point, point);

        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void computeDistance_knownDistance_returnsExpectedValue() {
        var paris = new Coordinates(48.8566, 2.3522);    // Paris
        var london = new Coordinates(51.5074, -0.1278);  // London

        double distance = distanceService.computeDistance(paris, london);

        assertEquals(344.0, distance, 1.0);
    }

    @Test
    void computeDistance_antipodes_returnsMaximumDistance() {
        var point1 = new Coordinates(0.0, 0.0);          // Point sur l'équateur
        var point2 = new Coordinates(0.0, 180.0);        // Point opposé

        double distance = distanceService.computeDistance(point1, point2);

        assertEquals(20015.0, distance, 1.0);
    }

    @Test
    void computeDistance_symmetry_returnsEqualDistance() {
        var tokyo = new Coordinates(35.6762, 139.6503);   // Tokyo
        var sydney = new Coordinates(-33.8688, 151.2093); // Sydney

        double distanceAtoB = distanceService.computeDistance(tokyo, sydney);
        double distanceBtoA = distanceService.computeDistance(sydney, tokyo);

        assertEquals(distanceAtoB, distanceBtoA, 0.0001);
    }

    @Test
    void computeDistance_nullCoordinates_throwsException() {
        var paris = new Coordinates(48.8566, 2.3522);

        assertThrows(NullPointerException.class, () ->
                distanceService.computeDistance(null, paris));
        assertThrows(NullPointerException.class, () ->
                distanceService.computeDistance(paris, null));
        assertThrows(NullPointerException.class, () ->
                distanceService.computeDistance(null, null));
    }
}