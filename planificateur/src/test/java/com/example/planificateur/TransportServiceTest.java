package com.example.planificateur;

import com.example.planificateur.criteria.TransportCriteria;
import com.example.planificateur.domain.ModeTransport;
import com.example.planificateur.domain.Transport;
import com.example.planificateur.repository.TransportRepository;
import com.example.planificateur.service.TransportService;
import com.example.planificateur.service.TransportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class TransportServiceImplTest {

    @Mock
    private TransportRepository transportRepository;

    private TransportServiceImpl transportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transportService = new TransportServiceImpl(transportRepository);
    }

    @Test
    void findTransports_shouldFilterByCity() {
        TransportCriteria criteria = new TransportCriteria();
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 15, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 16, 0, 0);
        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(3), ModeTransport.TRAIN, 100),
                new Transport("Lyon", "Paris", departureMin.plusHours(2), departureMin.plusHours(4), ModeTransport.TRAIN, 100)
        );
        when(transportRepository.findAll()).thenReturn(transports);

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureMin, departureMax);

        assertEquals(1, result.size());
        assertEquals("Paris", result.get(0).getCityFrom());
        assertEquals("Lyon", result.get(0).getCityTo());
    }

    @Test
    void findTransports_shouldFilterByDate() {
        TransportCriteria criteria = new TransportCriteria();
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 15, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 16, 0, 0);
        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", departureMin.minusDays(1), departureMin.plusHours(2), ModeTransport.TRAIN, 100),
                new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(3), ModeTransport.TRAIN, 100),
                new Transport("Paris", "Lyon", departureMax.plusDays(1), departureMax.plusHours(2), ModeTransport.TRAIN, 100)
        );
        when(transportRepository.findAll()).thenReturn(transports);

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureMin, departureMax);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getDepartureDateTime().isAfter(departureMin) || result.get(0).getDepartureDateTime().isEqual(departureMin));
        assertTrue(result.get(0).getDepartureDateTime().isBefore(departureMax) || result.get(0).getDepartureDateTime().isEqual(departureMax));
    }

    @Test
    void findTransports_shouldFilterByMode() {
        TransportCriteria criteria = new TransportCriteria(ModeTransport.TRAIN, false, false);
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 15, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 16, 0, 0);
        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(3), ModeTransport.TRAIN, 100),
                new Transport("Paris", "Lyon", departureMin.plusHours(2), departureMin.plusHours(4), ModeTransport.AVION, 200)
        );
        when(transportRepository.findAll()).thenReturn(transports);

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureMin, departureMax);

        assertEquals(1, result.size());
        assertEquals(ModeTransport.TRAIN, result.get(0).getMode());
    }

    @Test
    void findTransports_shouldSortByCheapestAndShortest() {
        TransportCriteria criteria = new TransportCriteria();
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 15, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 16, 0, 0);
        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(4), ModeTransport.TRAIN, 150),
                new Transport("Paris", "Lyon", departureMin.plusHours(2), departureMin.plusHours(4), ModeTransport.TRAIN, 100),
                new Transport("Paris", "Lyon", departureMin.plusHours(3), departureMin.plusHours(5), ModeTransport.TRAIN, 100)
        );
        when(transportRepository.findAll()).thenReturn(transports);

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureMin, departureMax);

        assertEquals(3, result.size());
        assertEquals(100, result.get(0).getPrice());
        assertEquals(2, result.get(0).getArrivalDateTime().getHour() - result.get(0).getDepartureDateTime().getHour());
    }

    @Test
    void findTransports_shouldHandleMultiStepJourney() {
        TransportCriteria criteria = new TransportCriteria();
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 15, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 16, 0, 0);
        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(3), ModeTransport.TRAIN, 80),
                new Transport("Lyon", "Marseille", departureMin.plusHours(4), departureMin.plusHours(6), ModeTransport.TRAIN, 70)
        );
        when(transportRepository.findAll()).thenReturn(transports);

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Marseille", departureMin, departureMax);

        assertEquals(2, result.size());
        assertEquals("Paris", result.get(0).getCityFrom());
        assertEquals("Lyon", result.get(0).getCityTo());
        assertEquals("Lyon", result.get(1).getCityFrom());
        assertEquals("Marseille", result.get(1).getCityTo());
    }


    @Test
    void testMultiStepJourneyConsistency() {
        // Arrange
        TransportRepository mockRepository = mock(TransportRepository.class);
        TransportService transportService = new TransportServiceImpl(mockRepository);

        List<Transport> transports = Arrays.asList(
                new Transport("Bordeaux", "Paris", LocalDateTime.of(2025, 1, 15, 9, 0),
                        LocalDateTime.of(2025, 1, 15, 12, 0), ModeTransport.TRAIN, 80.0),
                new Transport("Paris", "Rennes", LocalDateTime.of(2025, 1, 15, 13, 0),
                        LocalDateTime.of(2025, 1, 15, 15, 0), ModeTransport.TRAIN, 60.0),
                new Transport("Bordeaux", "Lyon", LocalDateTime.of(2025, 1, 15, 9, 0),
                        LocalDateTime.of(2025, 1, 15, 11, 0), ModeTransport.TRAIN, 70.0)
        );
        when(mockRepository.findAll()).thenReturn(transports);

        TransportCriteria criteria = new TransportCriteria(ModeTransport.TRAIN, false, false);
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 15, 8, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 15, 18, 0);

        // Act
        List<Transport> result = transportService.findTransports(criteria, "Bordeaux", "Rennes", departureMin, departureMax);

        // Assert
        assertEquals(2, result.size(), "Le voyage devrait comporter deux étapes");
        assertEquals("Bordeaux", result.get(0).getCityFrom(), "La première étape devrait partir de Bordeaux");
        assertEquals("Paris", result.get(0).getCityTo(), "La première étape devrait arriver à Paris");
        assertEquals("Paris", result.get(1).getCityFrom(), "La deuxième étape devrait partir de Paris");
        assertEquals("Rennes", result.get(1).getCityTo(), "La deuxième étape devrait arriver à Rennes");
    }

    @Test
    void testHomogeneousTransportModeForJourney() {
        // Arrange
        TransportRepository mockRepository = mock(TransportRepository.class);
        TransportService transportService = new TransportServiceImpl(mockRepository);

        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", LocalDateTime.of(2025, 1, 15, 9, 0),
                        LocalDateTime.of(2025, 1, 15, 11, 0), ModeTransport.TRAIN, 50.0),
                new Transport("Lyon", "Marseille", LocalDateTime.of(2025, 1, 15, 12, 0),
                        LocalDateTime.of(2025, 1, 15, 14, 0), ModeTransport.TRAIN, 40.0),
                new Transport("Lyon", "Marseille", LocalDateTime.of(2025, 1, 15, 11, 30),
                        LocalDateTime.of(2025, 1, 15, 12, 30), ModeTransport.AVION, 80.0)
        );
        when(mockRepository.findAll()).thenReturn(transports);

        TransportCriteria criteria = new TransportCriteria(ModeTransport.TRAIN, false, false);
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 15, 8, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 15, 18, 0);

        // Act
        List<Transport> result = transportService.findTransports(criteria, "Paris", "Marseille", departureMin, departureMax);

        // Assert
        assertEquals(2, result.size(), "Le voyage devrait comporter deux étapes");
        assertTrue(result.stream().allMatch(t -> t.getMode() == ModeTransport.TRAIN), "Tous les trajets devraient être en train");
        assertEquals("Paris", result.get(0).getCityFrom(), "Le premier trajet devrait partir de Paris");
        assertEquals("Lyon", result.get(0).getCityTo(), "Le premier trajet devrait arriver à Lyon");
        assertEquals("Lyon", result.get(1).getCityFrom(), "Le second trajet devrait partir de Lyon");
        assertEquals("Marseille", result.get(1).getCityTo(), "Le second trajet devrait arriver à Marseille");
    }

    @Test
    void findTransports_shouldPreferCheapestWhenEqualDuration() {
        TransportCriteria criteria = new TransportCriteria();
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 2, 0, 0);
        Transport expensive = new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(3), ModeTransport.TRAIN, 150);
        Transport cheap = new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(3), ModeTransport.TRAIN, 100);
        when(transportRepository.findAll()).thenReturn(Arrays.asList(expensive, cheap));

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureMin, departureMax);

        assertEquals(2, result.size());
        assertEquals(cheap, result.get(0));
        assertEquals(expensive, result.get(1));
    }

    @Test
    void findTransports_shouldPreferShortestTripWhenEqualPrice() {
        TransportCriteria criteria = new TransportCriteria();
        LocalDateTime departureTime = LocalDateTime.of(2025, 1, 15, 10, 0);
        Transport long_trip = new Transport("Paris", "Lyon", departureTime, departureTime.plusHours(4), ModeTransport.TRAIN, 100.0);
        Transport short_trip = new Transport("Paris", "Lyon", departureTime, departureTime.plusHours(3), ModeTransport.TRAIN, 100.0);
        when(transportRepository.findAll()).thenReturn(Arrays.asList(long_trip, short_trip));

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureTime, departureTime.plusHours(5));

        assertEquals(2, result.size());
        assertEquals(short_trip, result.get(0));
        assertEquals(long_trip, result.get(1));
        assertEquals(3, Duration.between(result.get(0).getDepartureDateTime(), result.get(0).getArrivalDateTime()).toHours());
    }
}



