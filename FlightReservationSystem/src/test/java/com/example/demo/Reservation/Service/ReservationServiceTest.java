package com.example.demo.Reservation.Service;

import com.example.demo.EmailService;
import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Passenger.Service.PassengerService;
import com.example.demo.Reservation.Entity.Reservation;
import com.example.demo.Reservation.Entity.ReservationDTO;
import com.example.demo.Reservation.Repository.ReservationRepository;
import com.example.demo.Passenger.Repository.PassengerRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerService passengerService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private Passenger passenger;
    private Flight flight;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setId(UUID.randomUUID());
        passenger.setEmail("jan@example.com");

        flight = new Flight();
        flight.setFlightNumber("LOT100");
        flight.setDate(LocalDate.now());

        reservation = new Reservation();
        reservation.setId(UUID.randomUUID());
        reservation.setReservationNumber("RES123");
        reservation.setPassenger(passenger);
        reservation.setFlight(flight);
        reservation.setIsDeparted(false);
    }

    @Test
    void shouldReturnAllReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Reservation> result = reservationService.findAll();

        assertEquals(1, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    void shouldReturnReservationById() {
        UUID id = reservation.getId();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.findById(id);

        assertEquals("RES123", result.getReservationNumber());
        verify(reservationRepository).findById(id);
    }

    @Test
    void shouldThrowWhenReservationNotFoundById() {
        UUID id = UUID.randomUUID();
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reservationService.findById(id);
        });

        assertTrue(ex.getMessage().contains("Reservation not found"));
    }

    @Test
    void shouldSaveReservationAndSendEmail() throws MessagingException {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.save(reservation);

        assertEquals("RES123", result.getReservationNumber());
        verify(emailService).sendReservationConfirmation(
                passenger.getEmail(),
                reservation.getReservationNumber(),
                flight.getFlightNumber(),
                flight.getDate()
        );
        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldUpdateReservation() {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationNumber("RES123");
        dto.setPassengerEmail("jan@example.com");
        dto.setIsDeparted(true);

        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(Optional.of(reservation));
        when(passengerService.findByEmail("jan@example.com")).thenReturn(Optional.of(passenger));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.update("RES123", dto);

        assertTrue(result.getIsDeparted());
        assertEquals(passenger, result.getPassenger());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentReservation() {
        ReservationDTO dto = new ReservationDTO();
        dto.setPassengerEmail("jan@example.com");

        when(reservationRepository.findByReservationNumber("NOT_FOUND")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reservationService.update("NOT_FOUND", dto);
        });
    }

    @Test
    void shouldThrowWhenPassengerNotFoundInUpdate() {
        ReservationDTO dto = new ReservationDTO();
        dto.setPassengerEmail("missing@example.com");

        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(Optional.of(reservation));
        when(passengerService.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            reservationService.update("RES123", dto);
        });
    }

    @Test
    void shouldDeleteReservationByReservationNumber() {
        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(Optional.of(reservation));

        reservationService.deleteByReservationNumber("RES123");

        verify(reservationRepository).deleteByReservationNumber("RES123");
    }

    @Test
    void shouldThrowWhenDeletingNonExistentReservation() {
        when(reservationRepository.findByReservationNumber("INVALID")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            reservationService.deleteByReservationNumber("INVALID");
        });

        assertTrue(ex.getMessage().contains("reservation not found"));
    }

    @Test
    void shouldReturnTrueIfReservationExistsByFlight() {
        when(reservationRepository.existsByFlight(flight)).thenReturn(true);

        boolean exists = reservationService.existsByFlight(flight);

        assertTrue(exists);
        verify(reservationRepository).existsByFlight(flight);
    }

    @Test
    void shouldFindByFlight() {
        when(reservationRepository.findByFlight(flight)).thenReturn(Optional.of(reservation));

        Optional<Reservation> result = reservationService.findByFlight(flight);

        assertTrue(result.isPresent());
        assertEquals("RES123", result.get().getReservationNumber());
    }

    @Test
    void shouldFindByReservationNumber() {
        when(reservationRepository.findByReservationNumber("RES123")).thenReturn(Optional.of(reservation));

        Optional<Reservation> result = reservationService.findByReservationNumber("RES123");

        assertTrue(result.isPresent());
        assertEquals("RES123", result.get().getReservationNumber());
    }
}
