package com.example.planificateur.service;

import com.example.planificateur.criteria.TransportCriteria;
import com.example.planificateur.domain.Transport;
import com.example.planificateur.domain.ModeTransport;
import com.example.planificateur.repository.TransportRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportServiceImpl implements TransportService {

    private final TransportRepository transportRepository;

    public TransportServiceImpl(TransportRepository transportRepository) {
        this.transportRepository = transportRepository;
    }

    /*@Override
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
    }*/
    @Override
    public List<Transport> findTransports(TransportCriteria criteria, String cityFrom, String cityTo, LocalDateTime departureMin, LocalDateTime departureMax) {
        List<Transport> all = transportRepository.findAll();
        List<Transport> result = new ArrayList<>();

        // Find direct route
        List<Transport> direct = all.stream()
                .filter(t -> t.getCityFrom().equalsIgnoreCase(cityFrom) && t.getCityTo().equalsIgnoreCase(cityTo))
                .filter(t -> !t.getDepartureDateTime().isBefore(departureMin) && !t.getDepartureDateTime().isAfter(departureMax))
                .filter(t -> criteria.getPreferredMode() == null || t.getMode() == criteria.getPreferredMode())
                .collect(Collectors.toList());

        if (!direct.isEmpty()) {
            result.addAll(direct);
        } else {
            // Find multi-step route
            List<Transport> firstLeg = findLeg(all, cityFrom, departureMin, departureMax, criteria);
            if (!firstLeg.isEmpty()) {
                Transport first = firstLeg.get(0);
                List<Transport> secondLeg = findLeg(all, first.getCityTo(), first.getArrivalDateTime(), departureMax, criteria);
                if (!secondLeg.isEmpty() && secondLeg.get(0).getCityTo().equalsIgnoreCase(cityTo)) {
                    result.add(first);
                    result.add(secondLeg.get(0));
                }
            }
        }

        // Apply sorting if necessary
        if (criteria.isPrioritizeCheapest()) {
            result.sort(Comparator.comparingDouble(Transport::getPrice));
        } else if (criteria.isPrioritizeShortest()) {
            result.sort(Comparator.comparingLong(t -> Duration.between(t.getDepartureDateTime(), t.getArrivalDateTime()).toMinutes()));
        }

        return result;
    }

    private List<Transport> findLeg(List<Transport> all, String cityFrom, LocalDateTime departureMin, LocalDateTime departureMax, TransportCriteria criteria) {
        return all.stream()
                .filter(t -> t.getCityFrom().equalsIgnoreCase(cityFrom))
                .filter(t -> !t.getDepartureDateTime().isBefore(departureMin) && !t.getDepartureDateTime().isAfter(departureMax))
                .filter(t -> criteria.getPreferredMode() == null || t.getMode() == criteria.getPreferredMode())
                .collect(Collectors.toList());
    }

}
