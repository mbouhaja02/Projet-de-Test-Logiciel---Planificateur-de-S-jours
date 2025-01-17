package com.example.planificateur.repository;

import com.example.planificateur.domain.ModeTransport;
import com.example.planificateur.domain.Transport;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CsvTransportRepository implements TransportRepository {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String CSV_RESOURCE_PATH = "/data/transports.csv";

    @Override
    public List<Transport> findAll() {
        List<Transport> transports = new ArrayList<>();
        try (BufferedReader reader = getBufferedReader()) {
            // Skip the first line if it's a header:
            reader.readLine(); // cityFrom;cityTo;departureDateTime;arrivalDateTime;mode;price

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 6) {
                    continue;
                }
                try {
                    String cityFrom = tokens[0];
                    String cityTo   = tokens[1];
                    LocalDateTime departure = LocalDateTime.parse(tokens[2], FORMATTER);
                    LocalDateTime arrival   = LocalDateTime.parse(tokens[3], FORMATTER);
                    ModeTransport mode      = ModeTransport.valueOf(tokens[4].toUpperCase());
                    double price            = Double.parseDouble(tokens[5]);

                    Transport t = new Transport(cityFrom, cityTo, departure, arrival, mode, price);
                    transports.add(t);
                } catch (Exception e) {
                    System.err.println("Invalid line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transports;
    }

    // New method to allow mocking in tests
    protected BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(CSV_RESOURCE_PATH),
                        "Fichier CSV introuvable : " + CSV_RESOURCE_PATH)));
    }
}
