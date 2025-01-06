package com.example.planificateur.repository;

import com.example.planificateur.domain.Hotel;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CsvHotelRepository implements HotelRepository {

    private final String csvPath = "/data/hotels.csv";

    @Override
    public List<Hotel> findAll() {
        List<Hotel> list = new ArrayList<>();

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
                if (tokens.length < 4) continue;

                String address = tokens[0];
                String city = tokens[1];
                int stars = Integer.parseInt(tokens[2]);
                double pricePerNight = Double.parseDouble(tokens[3]);

                list.add(new Hotel(address, city, stars, pricePerNight));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
