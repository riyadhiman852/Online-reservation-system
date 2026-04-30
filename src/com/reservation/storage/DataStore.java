package com.reservation.storage;

import com.reservation.model.Reservation;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

public class DataStore {
    private final Path dataDirectory = Paths.get("data");
    private final Path usersFile = dataDirectory.resolve("users.properties");
    private final Path reservationsFile = dataDirectory.resolve("reservations.csv");
    private final Map<String, String> trainCatalog = createTrainCatalog();

    public DataStore() {
        initializeStorage();
    }

    public boolean validateLogin(String username, String password) {
        Properties users = loadUsers();
        return password.equals(users.getProperty(username));
    }

    public Map<String, String> getTrainCatalog() {
        return trainCatalog;
    }

    public String createPnr() {
        return "PNR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    public void saveReservation(Reservation reservation) {
        List<String> lines = new ArrayList<>();
        lines.add(reservation.toCsv());
        try {
            Files.write(
                    reservationsFile,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to save reservation.", exception);
        }
    }

    public Optional<Reservation> findReservationByPnr(String pnr) {
        return loadReservations().stream()
                .filter(reservation -> reservation.getPnr().equalsIgnoreCase(pnr))
                .findFirst();
    }

    public boolean cancelReservation(String pnr) {
        List<Reservation> reservations = loadReservations();
        boolean updated = false;

        for (Reservation reservation : reservations) {
            if (reservation.getPnr().equalsIgnoreCase(pnr) && !reservation.isCancelled()) {
                reservation.setCancelled(true);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeReservations(reservations);
        }
        return updated;
    }

    private void initializeStorage() {
        try {
            Files.createDirectories(dataDirectory);
            if (Files.notExists(usersFile)) {
                Files.writeString(
                        usersFile,
                        "admin=admin123" + System.lineSeparator() + "staff=staff123" + System.lineSeparator(),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE);
            }
            if (Files.notExists(reservationsFile)) {
                Files.writeString(reservationsFile, "", StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to initialize storage.", exception);
        }
    }

    private Properties loadUsers() {
        Properties users = new Properties();
        try {
            users.load(Files.newBufferedReader(usersFile, StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load users.", exception);
        }
        return users;
    }

    private List<Reservation> loadReservations() {
        try {
            List<String> lines = Files.readAllLines(reservationsFile, StandardCharsets.UTF_8);
            List<Reservation> reservations = new ArrayList<>();
            for (String line : lines) {
                if (!line.isBlank()) {
                    reservations.add(Reservation.fromCsv(line));
                }
            }
            return reservations;
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read reservations.", exception);
        }
    }

    private void writeReservations(List<Reservation> reservations) {
        List<String> lines = reservations.stream().map(Reservation::toCsv).toList();
        try {
            Files.write(
                    reservationsFile,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to update reservations.", exception);
        }
    }

    private Map<String, String> createTrainCatalog() {
        Map<String, String> trains = new LinkedHashMap<>();
        trains.put("12001", "Shatabdi Express");
        trains.put("12951", "Mumbai Rajdhani");
        trains.put("12627", "Karnataka Express");
        trains.put("12839", "Chennai Mail");
        trains.put("22436", "Vande Bharat Express");
        return trains;
    }
}
