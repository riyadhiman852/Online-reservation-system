package com.reservation.model;

public class Reservation {
    private final String pnr;
    private final String passengerName;
    private final int age;
    private final String gender;
    private final String trainNumber;
    private final String trainName;
    private final String classType;
    private final String journeyDate;
    private final String fromPlace;
    private final String destination;
    private boolean cancelled;

    public Reservation(
            String pnr,
            String passengerName,
            int age,
            String gender,
            String trainNumber,
            String trainName,
            String classType,
            String journeyDate,
            String fromPlace,
            String destination,
            boolean cancelled) {
        this.pnr = pnr;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.classType = classType;
        this.journeyDate = journeyDate;
        this.fromPlace = fromPlace;
        this.destination = destination;
        this.cancelled = cancelled;
    }

    public String getPnr() {
        return pnr;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getClassType() {
        return classType;
    }

    public String getJourneyDate() {
        return journeyDate;
    }

    public String getFromPlace() {
        return fromPlace;
    }

    public String getDestination() {
        return destination;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String toCsv() {
        return String.join(",",
                escape(pnr),
                escape(passengerName),
                String.valueOf(age),
                escape(gender),
                escape(trainNumber),
                escape(trainName),
                escape(classType),
                escape(journeyDate),
                escape(fromPlace),
                escape(destination),
                String.valueOf(cancelled));
    }

    public static Reservation fromCsv(String line) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        return new Reservation(
                unescape(parts[0]),
                unescape(parts[1]),
                Integer.parseInt(parts[2]),
                unescape(parts[3]),
                unescape(parts[4]),
                unescape(parts[5]),
                unescape(parts[6]),
                unescape(parts[7]),
                unescape(parts[8]),
                unescape(parts[9]),
                Boolean.parseBoolean(parts[10]));
    }

    private static String escape(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private static String unescape(String value) {
        String trimmed = value;
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed.replace("\"\"", "\"");
    }
}
