package com.example.planificateur;

import com.example.planificateur.criteria.HotelCriteria;
import com.example.planificateur.domain.Hotel;
import com.example.planificateur.repository.HotelRepository;
import com.example.planificateur.service.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hotelService = new HotelServiceImpl(hotelRepository);
    }

    @Test
    void findHotels_shouldFilterByCity() {
        HotelCriteria criteria = new HotelCriteria();
        LocalDate checkIn = LocalDate.of(2025, 1, 15);
        LocalDate checkOut = LocalDate.of(2025, 1, 20);
        List<Hotel> hotels = Arrays.asList(
                new Hotel("123 Main St", "Paris", 3, 100.0),
                new Hotel("456 Elm St", "Lyon", 4, 150.0)
        );
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.findHotels(criteria, "Paris", checkIn, checkOut);

        assertEquals(1, result.size());
        assertEquals("Paris", result.get(0).getCity());
    }

    @Test
    void findHotels_shouldFilterByMinStars() {
        HotelCriteria criteria = new HotelCriteria();
        criteria.setMinStars(4);
        LocalDate checkIn = LocalDate.of(2025, 1, 15);
        LocalDate checkOut = LocalDate.of(2025, 1, 20);
        List<Hotel> hotels = Arrays.asList(
                new Hotel("123 Main St", "Paris", 3, 100.0),
                new Hotel("456 Elm St", "Paris", 4, 150.0),
                new Hotel("789 Oak St", "Paris", 5, 200.0)
        );
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.findHotels(criteria, "Paris", checkIn, checkOut);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(h -> h.getStars() >= 4));
    }

    @Test
    void findHotels_shouldSortByCheapest() {
        HotelCriteria criteria = new HotelCriteria();
        criteria.setPrioritizeCheapest(true);
        LocalDate checkIn = LocalDate.of(2025, 1, 15);
        LocalDate checkOut = LocalDate.of(2025, 1, 20);
        List<Hotel> hotels = Arrays.asList(
                new Hotel("123 Main St", "Paris", 3, 150.0),
                new Hotel("456 Elm St", "Paris", 4, 100.0),
                new Hotel("789 Oak St", "Paris", 5, 200.0)
        );
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.findHotels(criteria, "Paris", checkIn, checkOut);

        assertEquals(3, result.size());
        assertEquals(100.0, result.get(0).getPricePerNight());
        assertEquals(150.0, result.get(1).getPricePerNight());
        assertEquals(200.0, result.get(2).getPricePerNight());
    }

    @Test
    void findHotels_shouldSortByMaxStars() {
        HotelCriteria criteria = new HotelCriteria();
        criteria.setPrioritizeMaxStars(true);
        LocalDate checkIn = LocalDate.of(2025, 1, 15);
        LocalDate checkOut = LocalDate.of(2025, 1, 20);
        List<Hotel> hotels = Arrays.asList(
                new Hotel("123 Main St", "Paris", 3, 100.0),
                new Hotel("456 Elm St", "Paris", 5, 200.0),
                new Hotel("789 Oak St", "Paris", 4, 150.0)
        );
        when(hotelRepository.findAll()).thenReturn(hotels);

        List<Hotel> result = hotelService.findHotels(criteria, "Paris", checkIn, checkOut);

        assertEquals(3, result.size());
        assertEquals(5, result.get(0).getStars());
        assertEquals(4, result.get(1).getStars());
        assertEquals(3, result.get(2).getStars());
    }
}
