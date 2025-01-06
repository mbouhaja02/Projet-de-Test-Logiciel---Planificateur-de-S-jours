package com.example.planificateur.service;

import com.example.planificateur.criteria.HotelCriteria;
import com.example.planificateur.domain.Hotel;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {
    List<Hotel> findHotels(HotelCriteria criteria,
                           String city,
                           LocalDate checkInDate,
                           LocalDate checkOutDate);
}
