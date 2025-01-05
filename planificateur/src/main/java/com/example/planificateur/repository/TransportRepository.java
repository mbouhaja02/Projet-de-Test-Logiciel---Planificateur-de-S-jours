package com.example.planificateur.repository;

import com.example.planificateur.domain.Transport;
import java.util.List;

public interface TransportRepository {
    /**
     * @return la liste de tous les trajets depuis la source de données.
     */
    List<Transport> findAll();
}
