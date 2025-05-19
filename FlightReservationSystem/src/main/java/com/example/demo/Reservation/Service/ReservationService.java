package com.example.demo.Reservation.Service;

import com.example.demo.EmailService;
import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Passenger.Service.PassengerService;
import com.example.demo.Reservation.Entity.Reservation;
import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Passenger.Repository.PassengerRepository;
import com.example.demo.Reservation.Entity.ReservationDTO;
import com.example.demo.Reservation.Repository.ReservationRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final PassengerRepository passengerRepository;
    private final PassengerService passengerService;
    private final EmailService emailService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, PassengerRepository passengerRepository,  PassengerService passengerService, EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.passengerRepository = passengerRepository;
        this.passengerService = passengerService;
        this.emailService = emailService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
    }


    public Reservation save(Reservation reservation) throws MessagingException {
        emailService.sendReservationConfirmation(
                reservation.getPassenger().getEmail(),
                reservation.getReservationNumber(),
                reservation.getFlight().getFlightNumber(),
                reservation.getFlight().getDate()
        );
        return reservationRepository.save(reservation);
    }

    public Reservation update(String reservationNumber, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));


        Passenger updatedPassenger = passengerService.findByEmail(dto.getPassengerEmail())
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        reservation.setPassenger(updatedPassenger);
        reservation.setIsDeparted(dto.getIsDeparted());

        return reservationRepository.save(reservation);
    }


    @Transactional
    public void deleteByReservationNumber(String reservationNumber) {
        Reservation reservation = reservationRepository.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new RuntimeException("reservation not found with title: " + reservationNumber));
        reservationRepository.deleteByReservationNumber(reservationNumber);
    }

    public boolean existsByFlight(Flight flight) {
        return reservationRepository.existsByFlight(flight);
    }

    public Optional<Reservation> findByFlight(Flight flight) {
        return reservationRepository.findByFlight(flight);
    }

    public Optional<Reservation> findByReservationNumber(String reservationNumber) {
        return reservationRepository.findByReservationNumber(reservationNumber);
    }

}
