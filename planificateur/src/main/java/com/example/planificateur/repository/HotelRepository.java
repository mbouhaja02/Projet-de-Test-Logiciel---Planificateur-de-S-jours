package com.example.planificateur.repository;

import com.example.planificateur.domain.Hotel;
import java.util.List;

public interface HotelRepository {
    List<Hotel> findAll();
}
