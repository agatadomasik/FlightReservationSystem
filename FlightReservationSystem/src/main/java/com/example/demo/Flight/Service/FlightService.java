package com.example.demo.Flight.Service;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Flight.Entity.FlightDTO;
import com.example.demo.Flight.Repository.FlightRepository;
import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Passenger.Entity.PassengerDTO;
import com.example.demo.Passenger.Repository.PassengerRepository;
import com.example.demo.Reservation.Repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;


    @Autowired
    public FlightService(FlightRepository flightRepository, ReservationRepository reservationRepository) {
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<FlightDTO> findAll() {
        List<Flight> flights = flightRepository.findAll();
        return flights.stream()
                .map(f -> new FlightDTO(
                        f.getDepartureLocation(),
                        f.getArrivalLocation(),
                        f.getFlightDuration(),
                        f.getDate(),
                        f.getFlightNumber(),
                        f.isRoundTrip(),
                        f.getSeatNumber()))
                .collect(Collectors.toList());
    }

    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    @Transactional
    public void deleteByFlightNumberAndSeatNumber(String flightNumber, String seatNumber) {
        Flight flight = flightRepository.findByFlightNumberAndSeatNumber(flightNumber, seatNumber)
                .orElseThrow(() -> new RuntimeException("Flight not found with flight number '" + flightNumber
                + "' and seat number '" + seatNumber +"'"));

        reservationRepository.deleteAllByFlightId(flight.getId());
        flightRepository.deleteByFlightNumberAndSeatNumber(flightNumber, seatNumber);
    }

    public Flight findById(UUID id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
    }

    public Optional<Flight> findByFlightNumberAndSeatNumber(String flightNumber, String seatNumber) {
        return flightRepository.findByFlightNumberAndSeatNumber(flightNumber, seatNumber);
    }


    public Flight update(String flightNumber, String seatNumber, Flight updatedFlight) {
        Flight existingFlight = flightRepository.findByFlightNumberAndSeatNumber(flightNumber, seatNumber)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        existingFlight.setDepartureLocation(updatedFlight.getDepartureLocation());
        existingFlight.setArrivalLocation(updatedFlight.getArrivalLocation());
        existingFlight.setFlightDuration(updatedFlight.getFlightDuration());
        existingFlight.setDate(updatedFlight.getDate());
        existingFlight.setRoundTrip(updatedFlight.isRoundTrip());


        return flightRepository.save(existingFlight);
    }

}
