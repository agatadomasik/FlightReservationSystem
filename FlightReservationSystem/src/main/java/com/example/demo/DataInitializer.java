package com.example.demo;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Flight.Service.FlightService;
import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Reservation.Service.ReservationService;
import com.example.demo.Reservation.Entity.Reservation;
import com.example.demo.Passenger.Service.PassengerService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer {

    private final PassengerService passengerService;
    private final ReservationService reservationService;
    private final FlightService flightService;

    @Autowired
    public DataInitializer(PassengerService passengerService, ReservationService reservationService, FlightService flightService) {
        this.passengerService = passengerService;
        this.reservationService = reservationService;
        this.flightService = flightService;
    }

    @PostConstruct
    public void initialize() throws MessagingException {
        Passenger p1 = new Passenger.PassengerBuilder()
                .setFirstName("Agata")
                .setLastName("Domasik")
                .setEmail("agata.domasik@gmail.com")
                .setPhone("123456789")
                .build();

        passengerService.save(p1);

        Flight f1 = new Flight.FlightBuilder()
                .setDepartureLocation("Warsaw")
                .setArrivalLocation("Paris")
                .setFlightDuration("01:21")
                .setDate(LocalDate.of(2025, 4, 15))
                .setFlightNumber("F1")
                .setIsRoundTrip(false)
                .setSeatNumber("S1")
                .build();
        Flight f2 = new Flight.FlightBuilder()
                .setDepartureLocation("Warsaw")
                .setArrivalLocation("Paris")
                .setFlightDuration("01:21")
                .setDate(LocalDate.of(2025, 4, 15))
                .setFlightNumber("F1")
                .setIsRoundTrip(false)
                .setSeatNumber("S2")
                .build();
        Flight f3 = new Flight.FlightBuilder()
                .setDepartureLocation("Gdansk")
                .setArrivalLocation("Krakow")
                .setFlightDuration("00:44")
                .setDate(LocalDate.of(2025, 4, 15))
                .setFlightNumber("F2")
                .setIsRoundTrip(false)
                .setSeatNumber("S1")
                .build();

        flightService.save(f1);
        flightService.save(f2);
        flightService.save(f3);


        Reservation reservation1 = new Reservation.ReservationBuilder()
                .setReservationNumber("R1")
                .setFlight(f1)
                .setPassenger(p1)
                .setIsDeparted(false)
                .build();


        reservationService.save(reservation1);
    }
}
