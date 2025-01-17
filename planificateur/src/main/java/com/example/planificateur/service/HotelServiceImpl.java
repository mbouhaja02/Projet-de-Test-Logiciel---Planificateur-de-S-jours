package com.example.planificateur.service;

import com.example.planificateur.criteria.HotelCriteria;
import com.example.planificateur.domain.Hotel;
import com.example.planificateur.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> findHotels(HotelCriteria criteria,
                                  String city,
                                  LocalDate checkInDate,
                                  LocalDate checkOutDate) {

        List<Hotel> all = hotelRepository.findAll();

        int minStars = criteria.getMinStars() > 0 ? criteria.getMinStars() : 1;
        int maxStars = 5;

        List<Hotel> filtered = all.stream()
                .filter(h -> h.getCity() != null && h.getCity().equalsIgnoreCase(city))
                .filter(h -> h.getStars() >= minStars)
                .filter(h -> h.getStars() <= maxStars)
                .collect(Collectors.toList());

        if (Boolean.TRUE.equals(criteria.isPrioritizeCheapest())) {
            filtered.sort(Comparator.comparing(Hotel::getPricePerNight,
                    Comparator.nullsLast(Double::compareTo)));
        } else if (Boolean.TRUE.equals(criteria.isPrioritizeMaxStars())) {
            filtered.sort(Comparator.comparing(Hotel::getStars,
                    Comparator.nullsLast(Integer::compareTo)).reversed());
        }

        return filtered;
    }
}