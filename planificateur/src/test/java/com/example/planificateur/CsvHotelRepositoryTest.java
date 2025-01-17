package com.example.planificateur;

import com.example.planificateur.domain.Hotel;
import com.example.planificateur.repository.CsvHotelRepository;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvHotelRepositoryTest {

    @Test
    void findAll_returnsListOfHotels() throws Exception {
        String csvData = "address;city;stars;pricePerNight\n" +
                "123 Main St;Paris;5;200.0\n" +
                "456 Elm St;Lyon;4;150.0\n";

        BufferedReader mockReader = new BufferedReader(new StringReader(csvData));

        CsvHotelRepository repository = new CsvHotelRepository() {
            @Override
            protected BufferedReader getBufferedReader() {
                return mockReader;
            }
        };

        List<Hotel> hotels = repository.findAll();

        assertEquals(2, hotels.size(), "There should be 2 hotels loaded from CSV.");

        Hotel firstHotel = hotels.get(0);
        assertEquals("123 Main St", firstHotel.getAddress());
        assertEquals("Paris", firstHotel.getCity());
        assertEquals(5, firstHotel.getStars());
        assertEquals(200.0, firstHotel.getPricePerNight());

        Hotel secondHotel = hotels.get(1);
        assertEquals("456 Elm St", secondHotel.getAddress());
        assertEquals("Lyon", secondHotel.getCity());
        assertEquals(4, secondHotel.getStars());
        assertEquals(150.0, secondHotel.getPricePerNight());
    }

    @Test
    void findAll_invalidLine_skipsLine() throws Exception {
        String csvData = "address;city;stars;pricePerNight\n" +
                "123 Main St;Paris;-1;200.0\n"; // Invalid stars

        BufferedReader mockReader = new BufferedReader(new StringReader(csvData));

        CsvHotelRepository repository = new CsvHotelRepository() {
            @Override
            protected BufferedReader getBufferedReader() {
                return mockReader;
            }
        };

        List<Hotel> hotels = repository.findAll();

        assertEquals(0, hotels.size(), "There should be no hotels loaded due to invalid line.");
    }
}
