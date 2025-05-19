package com.example.demo.Passenger.Service;

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
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final ReservationRepository reservationRepository;


    @Autowired
    public PassengerService(PassengerRepository passengerRepository, ReservationRepository reservationRepository) {
        this.passengerRepository = passengerRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<PassengerDTO> findAll() {
        List<Passenger> passengers = passengerRepository.findAll();
        return passengers.stream()
                .map(p -> new PassengerDTO(
                        p.getFirstName(),
                        p.getLastName(),
                        p.getEmail(),
                        p.getPhone()))
                .collect(Collectors.toList());
    }

    public Passenger save(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Transactional
    public void deleteByEmail(String email) {
        Passenger passenger = passengerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Passenger not found with email: " + email));

        reservationRepository.deleteAllByPassengerId(passenger.getId());
        passengerRepository.deleteByEmail(email);
    }

    public Passenger findById(UUID id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + id));
    }

    public Optional<Passenger> findByEmail(String email) {
        return passengerRepository.findByEmail(email);
    }


    public Passenger update(String email, Passenger updatedPassenger) {
        Passenger existingPassenger = passengerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        existingPassenger.setFirstName(updatedPassenger.getFirstName());
        existingPassenger.setLastName(updatedPassenger.getLastName());
        existingPassenger.setPhone(updatedPassenger.getPhone());

        return passengerRepository.save(existingPassenger);
    }

}
