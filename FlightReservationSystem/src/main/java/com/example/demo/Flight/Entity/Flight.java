package com.example.demo.Flight.Entity;

import com.example.demo.Reservation.Entity.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="flights")
public class Flight implements Comparable<Flight>, Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="departure_location", nullable = false)
    private String departureLocation;

    @Column(name="arrival_location", nullable = false)
    private String arrivalLocation;

    @Column(name="flightDuration", nullable = false)
    private String flightDuration;

    @Column(name="date", nullable = false)
    private LocalDate date;

    @Column(name="flight_number", nullable = false)
    private String flightNumber;

    @Column(name="is_round_trip", nullable = false)
    private boolean isRoundTrip;

    @Column(name="seatNumber", nullable = false)
    private String seatNumber;

    @OneToOne(mappedBy = "flight", fetch = FetchType.EAGER,  cascade = CascadeType.ALL, orphanRemoval = true)
    Reservation reservation;

    private Flight(FlightBuilder builder) {
        this.departureLocation = builder.departureLocation;
        this.arrivalLocation = builder.arrivalLocation;
        this.flightDuration = builder.flightDuration;
        this.date = builder.date;
        this.flightNumber = builder.flightNumber;
        this.isRoundTrip = builder.isRoundTrip;
        this.seatNumber = builder.seatNumber;
        this.reservation = builder.reservation;
    }

    public void addReservation(Reservation reservation){
        this.reservation = reservation;
    }

    @Override
    public String toString(){
        return "{\n departure location: " + departureLocation
                + "\n arrival location: " + arrivalLocation
                + "\n flight duration: " + flightDuration
                + "\n flight number: " + flightNumber
                + "\n is it round trip: " + isRoundTrip
                + "\n seat number: " + seatNumber + "\n}";
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (other == null || other.getClass() != this.getClass()) return false;
        Flight f = (Flight) other;
        return departureLocation.equals(f.departureLocation) && arrivalLocation == f.arrivalLocation && reservation.equals(f.reservation);
    }

    @Override
    public int hashCode(){
        return Objects.hash(departureLocation, arrivalLocation);
    }

    @Override
    public int compareTo(Flight f) {
        if (departureLocation.compareTo(f.departureLocation)!=0) return departureLocation.compareTo(f.departureLocation);
        return arrivalLocation.compareTo(f.arrivalLocation);
    }

    public static class FlightBuilder {
        String departureLocation;
        String arrivalLocation;
        String flightDuration;
        LocalDate date;
        String flightNumber;
        boolean isRoundTrip;
        String seatNumber;
        Reservation reservation;

        public FlightBuilder setDepartureLocation(String departureLocation){
            this.departureLocation = departureLocation;
            return this;
        }
        public FlightBuilder setArrivalLocation(String arrivalLocation){
            this.arrivalLocation = arrivalLocation;
            return this;
        }
        public FlightBuilder setFlightDuration(String flightDuration){
            this.flightDuration = flightDuration;
            return this;
        }
        public FlightBuilder setDate(LocalDate date){
            this.date = date;
            return this;
        }
        public FlightBuilder setFlightNumber(String flightNumber){
            this.flightNumber = flightNumber;
            return this;
        }
        public FlightBuilder setIsRoundTrip(boolean isRoundTrip){
            this.isRoundTrip = isRoundTrip;
            return this;
        }
        public FlightBuilder setSeatNumber(String seatNumber){
            this.seatNumber = seatNumber;
            return this;
        }
        public FlightBuilder setReservation(Reservation reservation){
            this.reservation = reservation;
            return this;
        }

        public Flight build(){
            return new Flight(this);
        }
    }
}

