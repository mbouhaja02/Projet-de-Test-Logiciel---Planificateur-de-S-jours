package com.example.planificateur;

import com.example.planificateur.criteria.TransportCriteria;
import com.example.planificateur.domain.ModeTransport;
import com.example.planificateur.domain.Transport;
import com.example.planificateur.repository.TransportRepository;
import com.example.planificateur.service.TransportService;
import com.example.planificateur.service.TransportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 2, 0, 0);
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
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 2, 0, 0);
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
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 2, 0, 0);
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
    void findTransports_shouldSortByCheapest() {
        TransportCriteria criteria = new TransportCriteria(null, true, false);
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 2, 0, 0);
        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(3), ModeTransport.TRAIN, 150),
                new Transport("Paris", "Lyon", departureMin.plusHours(2), departureMin.plusHours(4), ModeTransport.TRAIN, 100)
        );
        when(transportRepository.findAll()).thenReturn(transports);

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureMin, departureMax);

        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getPrice());
        assertEquals(150, result.get(1).getPrice());
    }

    @Test
    void findTransports_shouldSortByShortest() {
        TransportCriteria criteria = new TransportCriteria(null, false, true);
        LocalDateTime departureMin = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime departureMax = LocalDateTime.of(2025, 1, 2, 0, 0);
        List<Transport> transports = Arrays.asList(
                new Transport("Paris", "Lyon", departureMin.plusHours(1), departureMin.plusHours(4), ModeTransport.TRAIN, 100),
                new Transport("Paris", "Lyon", departureMin.plusHours(2), departureMin.plusHours(4), ModeTransport.TRAIN, 100)
        );
        when(transportRepository.findAll()).thenReturn(transports);

        List<Transport> result = transportService.findTransports(criteria, "Paris", "Lyon", departureMin, departureMax);

        assertEquals(2, result.size());
        assertEquals(2, result.get(0).getArrivalDateTime().getHour() - result.get(0).getDepartureDateTime().getHour());
        assertEquals(3, result.get(1).getArrivalDateTime().getHour() - result.get(1).getDepartureDateTime().getHour());
    }
}
