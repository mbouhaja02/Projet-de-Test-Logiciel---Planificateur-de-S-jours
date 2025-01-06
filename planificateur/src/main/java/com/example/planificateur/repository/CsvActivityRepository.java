package com.example.planificateur.repository;

import com.example.planificateur.domain.Activity;
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
public class CsvActivityRepository implements ActivityRepository {

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String csvPath = "/data/activities.csv";

    @Override
    public List<Activity> findAll() {
        List<Activity> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(
                    getClass().getResourceAsStream(csvPath),
                    "Fichier CSV introuvable : " + csvPath
                )
            )
        )) {
            reader.readLine(); // ignorer header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 5) continue;

                String address = tokens[0];
                String city = tokens[1];
                LocalDateTime dateTime = LocalDateTime.parse(tokens[2], FORMATTER);
                String category = tokens[3];
                double price = Double.parseDouble(tokens[4]);

                list.add(new Activity(address, city, dateTime, category, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
