package com.example.planificateur;

import com.example.planificateur.domain.Activity;
import com.example.planificateur.repository.CsvActivityRepository;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvActivityRepositoryTest {

    @Test
    void findAll_returnsListOfActivities() throws Exception {
        String csvData = "address;city;dateTime;category;price\n" +
                "123 Main St;Paris;2025-01-15 08:00;Sport;50.0\n" +
                "456 Elm St;Lyon;2025-01-16 10:00;Music;30.0\n";

        BufferedReader mockReader = new BufferedReader(new StringReader(csvData));

        CsvActivityRepository repository = new CsvActivityRepository() {
            @Override
            protected BufferedReader getBufferedReader() {
                return mockReader;
            }
        };

        List<Activity> activities = repository.findAll();

        assertEquals(2, activities.size(), "There should be 2 activities loaded from CSV.");

        Activity firstActivity = activities.get(0);
        assertEquals("123 Main St", firstActivity.getAddress());
        assertEquals("Paris", firstActivity.getCity());
        assertEquals(LocalDateTime.of(2025, 1, 15, 8, 0), firstActivity.getDateTime());
        assertEquals("Sport", firstActivity.getCategory());
        assertEquals(50.0, firstActivity.getPrice());

        Activity secondActivity = activities.get(1);
        assertEquals("456 Elm St", secondActivity.getAddress());
        assertEquals("Lyon", secondActivity.getCity());
        assertEquals(LocalDateTime.of(2025, 1, 16, 10, 0), secondActivity.getDateTime());
        assertEquals("Music", secondActivity.getCategory());
        assertEquals(30.0, secondActivity.getPrice());
    }

    @Test
    void findAll_invalidLine_skipsLine() throws Exception {
        String csvData = "address;city;dateTime;category;price\n" +
                "123 Main St;Paris;invalid_date_time;Sport;50.0\n"; // Invalid date time

        BufferedReader mockReader = new BufferedReader(new StringReader(csvData));

        CsvActivityRepository repository = new CsvActivityRepository() {
            @Override
            protected BufferedReader getBufferedReader() {
                return mockReader;
            }
        };

        List<Activity> activities = repository.findAll();

        assertEquals(0, activities.size(), "There should be no activities loaded due to invalid line.");
    }
}
