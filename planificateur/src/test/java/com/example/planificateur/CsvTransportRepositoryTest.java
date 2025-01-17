package com.example.planificateur;

import com.example.planificateur.domain.ModeTransport;
import com.example.planificateur.domain.Transport;
import com.example.planificateur.repository.CsvTransportRepository;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvTransportRepositoryTest {

    @Test
    void findAll_returnsListOfTransports() throws Exception {
        String csvData = "cityFrom;cityTo;departureDateTime;arrivalDateTime;mode;price\n" +
                "Paris;Lyon;2025-01-15 08:00;2025-01-15 09:30;TRAIN;50.0\n" +
                "Lyon;Marseille;2025-01-16 10:00;2025-01-16 12:00;AVION;30.0\n";

        BufferedReader mockReader = new BufferedReader(new StringReader(csvData));

        CsvTransportRepository repository = new CsvTransportRepository() {
            @Override
            protected BufferedReader getBufferedReader() {
                return mockReader;
            }
        };

        List<Transport> transports = repository.findAll();

        assertEquals(2, transports.size(), "There should be 2 transports loaded from CSV.");

        Transport firstTransport = transports.get(0);
        assertEquals("Paris", firstTransport.getCityFrom());
        assertEquals("Lyon", firstTransport.getCityTo());
        assertEquals(LocalDateTime.of(2025, 1, 15, 8, 0), firstTransport.getDepartureDateTime());
        assertEquals(LocalDateTime.of(2025, 1, 15, 9, 30), firstTransport.getArrivalDateTime());
        assertEquals(ModeTransport.TRAIN, firstTransport.getMode());
        assertEquals(50.0, firstTransport.getPrice());

        Transport secondTransport = transports.get(1);
        assertEquals("Lyon", secondTransport.getCityFrom());
        assertEquals("Marseille", secondTransport.getCityTo());
        assertEquals(LocalDateTime.of(2025, 1, 16, 10, 0), secondTransport.getDepartureDateTime());
        assertEquals(LocalDateTime.of(2025, 1, 16, 12, 0), secondTransport.getArrivalDateTime());
        assertEquals(ModeTransport.AVION, secondTransport.getMode());
        assertEquals(30.0, secondTransport.getPrice());
    }

    @Test
    void findAll_invalidLine_skipsLine() throws Exception {
        String csvData = "cityFrom;cityTo;departureDateTime;arrivalDateTime;mode;price\n" +
                "Paris;Lyon;invalid_date_time;invalid_date_time;TRAIN;\n"; // Missing price

        BufferedReader mockReader = new BufferedReader(new StringReader(csvData));

        CsvTransportRepository repository = new CsvTransportRepository() {
            @Override
            protected BufferedReader getBufferedReader() {
                return mockReader;
            }
        };

        List<Transport> transports = repository.findAll();

        assertEquals(0, transports.size(), "There should be no transports loaded due to invalid line.");
    }
}
