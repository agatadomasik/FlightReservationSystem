package com.example.demo.Flight.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class FlightDTO {
    private String departureLocation;
    private String arrivalLocation;
    private String flightDuration;
    private LocalDate date;
    private String flightNumber;
    private boolean isRoundTrip;
    private String seatNumber;

    public FlightDTO(String departureLocation, String arrivalLocation, String flightDuration, LocalDate date, String flightNumber, boolean isRoundTrip, String seatNumber) {
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.flightDuration = flightDuration;
        this.date = date;
        this.flightNumber = flightNumber;
        this.isRoundTrip = isRoundTrip;
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "{\n departure location: " + departureLocation
                + "\n arrival location: " + arrivalLocation
                + "\n flight duration: " + flightDuration
                + "\n flight number: " + flightNumber
                + "\n is it round trip: " + isRoundTrip
                + "\n seat number: " + seatNumber + "\n}";
    }
}

