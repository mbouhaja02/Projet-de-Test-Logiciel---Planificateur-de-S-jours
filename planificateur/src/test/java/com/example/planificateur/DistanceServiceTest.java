package com.example.planificateur;

import com.example.planificateur.service.DistanceService;
import com.example.planificateur.service.DistanceServiceImpl;

import com.example.planificateur.service.GeocodingService;
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
        // Arrange
        var point = new GeocodingService.Coordinates(48.8566, 2.3522); // Paris

        // Act
        double distance = distanceService.computeDistance(point, point);

        // Assert
        assertEquals(0.0, distance, 0.0001);
    }

    @Test
    void computeDistance_knownDistance_returnsExpectedValue() {
        // Arrange
        var paris = new GeocodingService.Coordinates(48.8566, 2.3522);    // Paris
        var london = new GeocodingService.Coordinates(51.5074, -0.1278);  // London

        // Act
        double distance = distanceService.computeDistance(paris, london);

        // Assert
        // La distance réelle entre Paris et Londres est d'environ 344 km
        assertEquals(344.0, distance, 1.0);
    }

    @Test
    void computeDistance_antipodes_returnsMaximumDistance() {
        // Arrange
        var point1 = new GeocodingService.Coordinates(0.0, 0.0);          // Point sur l'équateur
        var point2 = new GeocodingService.Coordinates(0.0, 180.0);        // Point opposé

        // Act
        double distance = distanceService.computeDistance(point1, point2);

        // Assert
        // La moitié de la circonférence de la Terre
        assertEquals(20015.0, distance, 1.0);
    }

    @Test
    void computeDistance_symmetry_returnsEqualDistance() {
        // Arrange
        var tokyo = new GeocodingService.Coordinates(35.6762, 139.6503);   // Tokyo
        var sydney = new GeocodingService.Coordinates(-33.8688, 151.2093); // Sydney

        // Act
        double distanceAtoB = distanceService.computeDistance(tokyo, sydney);
        double distanceBtoA = distanceService.computeDistance(sydney, tokyo);

        // Assert
        assertEquals(distanceAtoB, distanceBtoA, 0.0001);
    }

    @Test
    void computeDistance_nullCoordinates_throwsException() {
        // Arrange
        var paris = new GeocodingService.Coordinates(48.8566, 2.3522);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                distanceService.computeDistance(null, paris));
        assertThrows(NullPointerException.class, () ->
                distanceService.computeDistance(paris, null));
        assertThrows(NullPointerException.class, () ->
                distanceService.computeDistance(null, null));
    }
}