package com.example.demo.Reservation.Entity;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Passenger.Entity.Passenger;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="reservations")
public class Reservation implements Comparable<Reservation>, Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "reservation_number", nullable = false)
    private String reservationNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="passenger_id", nullable = false)
    private Passenger passenger;

    @Column(name = "is_departed", nullable = false)
    private Boolean isDeparted;


    public Reservation(ReservationBuilder builder){
        this.reservationNumber = builder.reservationNumber;
        this.flight = builder.flight;
        this.passenger = builder.passenger;
        this.isDeparted = builder.isDeparted;
        passenger.addReservation(this);
    }

    @Override
    public String toString(){
        return "{\n reservation number: " + reservationNumber
                + "\n flight number: " + flight.getFlightNumber()
                + "\n seat number: " + flight.getSeatNumber()
                + "\n passenger first name: " + passenger.getFirstName()
                + "\n passenger last name: " + passenger.getLastName()
                + "\n passenger phone number: " + passenger.getPhone()
                + "\n is it departed: " + isDeparted + "\n}";
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (other == null || other.getClass() != this.getClass()) return false;
        Reservation c = (Reservation)other;
        return reservationNumber.equals(c.reservationNumber) && passenger.equals(c.passenger);
    }

    @Override
    public int hashCode(){
        return Objects.hash(reservationNumber, passenger);
    }

    @Override
    public int compareTo(Reservation o) {
        if (reservationNumber.compareTo(o.reservationNumber)!=0) return reservationNumber.compareTo(o.reservationNumber);
        return passenger.getFirstName().compareTo(o.passenger.getFirstName());
    }

    public static class ReservationBuilder {
        private String reservationNumber;
        private Flight flight;
        private Passenger passenger;
        private  boolean isDeparted;

        public ReservationBuilder setReservationNumber(String reservationNumber) {
            this.reservationNumber = reservationNumber;
            return this;
        }

        public ReservationBuilder setFlight(Flight flight) {
            this.flight = flight;
            return this;
        }

        public ReservationBuilder setPassenger(Passenger passenger) {
            this.passenger = passenger;
            return this;
        }

        public ReservationBuilder setIsDeparted(boolean isDeparted) {
            this.isDeparted = isDeparted;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }
}

