package com.example.demo.Reservation.Controller;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Flight.Service.FlightService;
import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Passenger.Service.PassengerService;
import com.example.demo.Reservation.Entity.ReservationCreateDTO;
import com.example.demo.Reservation.Entity.ReservationDTO;
import com.example.demo.Reservation.Entity.Reservation;
import com.example.demo.Reservation.Service.ReservationService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;
    private final PassengerService passengerService;
    private final FlightService flightService;


    @Autowired
    public ReservationController(ReservationService reservationService, PassengerService passengerService, FlightService flightService) {
        this.reservationService = reservationService;
        this.flightService = flightService;
        this.passengerService = passengerService;
    }

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.findAll().stream()
                .map(reservation -> {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setReservationNumber(reservation.getReservationNumber());
                    dto.setFlightNumber(reservation.getFlight().getFlightNumber());
                    dto.setSeatNumber(reservation.getFlight().getSeatNumber());
                    dto.setPassengerEmail(reservation.getPassenger().getEmail());
                    dto.setPassengerFirstName(reservation.getPassenger().getFirstName());
                    dto.setPassengerLastName(reservation.getPassenger().getLastName());
                    dto.setPassengerPhone(reservation.getPassenger().getPhone());
                    dto.setIsDeparted(reservation.getIsDeparted());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ReservationDTO getReservationById(@PathVariable UUID id) {
        return convertToDTO(reservationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationCreateDTO request) throws MessagingException {
        Flight flight = flightService.findByFlightNumberAndSeatNumber(request.getFlightNumber(), request.getSeatNumber())
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        Passenger passenger = passengerService.findByEmail(request.getPassengerEmail())
                .orElseThrow(() -> new RuntimeException("Passenger not found"));

        Reservation reservation = new Reservation();
        reservation.setReservationNumber(request.getReservationNumber());
        reservation.setFlight(flight);
        reservation.setPassenger(passenger);
        reservation.setIsDeparted(request.getIsDeparted());

        Reservation saved = reservationService.save(reservation);
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationNumber(saved.getReservationNumber());
        reservationDTO.setFlightNumber(saved.getFlight().getFlightNumber());
        reservationDTO.setSeatNumber(saved.getFlight().getSeatNumber()); // jeśli seatNumber znajduje się w Flight
        reservationDTO.setPassengerFirstName(saved.getPassenger().getFirstName());
        reservationDTO.setPassengerLastName(saved.getPassenger().getLastName());
        reservationDTO.setPassengerEmail(saved.getPassenger().getEmail());
        reservationDTO.setPassengerPhone(saved.getPassenger().getPhone());

        return ResponseEntity.ok(reservationDTO);
    }


    @PutMapping("/{reservationNumber}")
    public ReservationDTO updateReservation(
            @PathVariable String reservationNumber,
            @RequestBody ReservationDTO updatedReservationDTO) {

        Reservation updated = reservationService.update(reservationNumber, updatedReservationDTO);
        System.out.println(updatedReservationDTO.getIsDeparted());
        System.out.println(updated);
        return convertToDTO(updated);
    }

    @GetMapping("/flight/{flightNumber}/seat/{seatNumber}")
    public ResponseEntity<ReservationDTO> getReservationByFlightAndSeat(
            @PathVariable String flightNumber,
            @PathVariable String seatNumber) {

        Flight flight = flightService.findByFlightNumberAndSeatNumber(flightNumber, seatNumber)
                .orElse(null);

        if (flight == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Reservation> optionalReservation = reservationService.findByFlight(flight);

        if (optionalReservation.isPresent()) {
            ReservationDTO reservationDTO = convertToDTO(optionalReservation.get());
            return ResponseEntity.ok(reservationDTO);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    @GetMapping("/number/{reservationNumber}")
    public Optional<ReservationDTO> getReservationByReservationNumber(@PathVariable String reservationNumber) {
        return reservationService.findByReservationNumber(reservationNumber)
                .map(this::convertToDTO);
    }

    @DeleteMapping
    public void deleteReservation(@RequestParam String reservationNumber) {
        reservationService.deleteByReservationNumber(reservationNumber);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getReservationNumber(),
                reservation.getFlight().getFlightNumber(),
                reservation.getFlight().getSeatNumber(),
                reservation.getPassenger().getFirstName(),
                reservation.getPassenger().getLastName(),
                reservation.getPassenger().getEmail(),
                reservation.getPassenger().getPhone(),
                reservation.getIsDeparted()
        );
    }
}
