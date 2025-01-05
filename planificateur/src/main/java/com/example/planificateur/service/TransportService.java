package com.example.planificateur.service;

import com.example.planificateur.criteria.TransportCriteria;
import com.example.planificateur.domain.Transport;
import java.time.LocalDateTime;
import java.util.List;

public interface TransportService {

    /**
     * Retourne la liste des transports correspondant aux critères
     * et aux paramètres (ville, date).
     */
    List<Transport> findTransports(TransportCriteria criteria,
                                   String cityFrom,
                                   String cityTo,
                                   LocalDateTime departureMin,
                                   LocalDateTime departureMax);
}
