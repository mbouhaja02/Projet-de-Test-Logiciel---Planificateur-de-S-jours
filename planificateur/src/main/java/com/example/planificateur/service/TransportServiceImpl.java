package com.example.planificateur.service;

import com.example.planificateur.criteria.TransportCriteria;
import com.example.planificateur.domain.Transport;
import com.example.planificateur.domain.ModeTransport;
import com.example.planificateur.repository.TransportRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportServiceImpl implements TransportService {

    private final TransportRepository transportRepository;

    public TransportServiceImpl(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    @Override
    public List<Transport> findTransports(TransportCriteria criteria,
                                          String cityFrom,
                                          String cityTo,
                                          LocalDateTime departureMin,
                                          LocalDateTime departureMax) {

        List<Transport> all = transportRepository.findAll();

        // Filtrage
        List<Transport> filtered = all.stream()
            .filter(t -> t.getCityFrom().equalsIgnoreCase(cityFrom))
            .filter(t -> t.getCityTo().equalsIgnoreCase(cityTo))
            .filter(t -> !t.getDepartureDateTime().isBefore(departureMin))
            .filter(t -> !t.getDepartureDateTime().isAfter(departureMax))
            .filter(t -> criteria.getPreferredMode() == null 
                     || t.getMode() == criteria.getPreferredMode())
            .collect(Collectors.toList());

        // Tri
        if (criteria.isPrioritizeCheapest()) {
            filtered.sort(Comparator.comparingDouble(Transport::getPrice));
        } else if (criteria.isPrioritizeShortest()) {
            filtered.sort((t1, t2) -> {
                long d1 = Duration.between(t1.getDepartureDateTime(), t1.getArrivalDateTime()).toMinutes();
                long d2 = Duration.between(t2.getDepartureDateTime(), t2.getArrivalDateTime()).toMinutes();
                return Long.compare(d1, d2);
            });
        }

        return filtered;
    }
}
