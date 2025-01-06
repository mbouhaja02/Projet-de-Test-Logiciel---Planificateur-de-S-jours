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
        // On ne se sert pas forcément des dates dans cette version simplifiée,
        // mais on pourrait par ex. filtrer sur la dispo.
        List<Hotel> all = hotelRepository.findAll();

        // Filtrage
        List<Hotel> filtered = all.stream()
            .filter(h -> h.getCity().equalsIgnoreCase(city))
            .filter(h -> h.getStars() >= criteria.getMinStars())
            .collect(Collectors.toList());

        // Tri
        if (criteria.isPrioritizeCheapest()) {
            filtered.sort(Comparator.comparingDouble(Hotel::getPricePerNight));
        } else if (criteria.isPrioritizeMaxStars()) {
            filtered.sort(Comparator.comparingInt(Hotel::getStars).reversed());
        }

        return filtered;
    }
}
