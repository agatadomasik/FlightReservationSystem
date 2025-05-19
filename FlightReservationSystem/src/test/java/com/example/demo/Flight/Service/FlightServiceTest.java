package com.example.demo.Flight.Service;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Flight.Entity.FlightDTO;
import com.example.demo.Flight.Repository.FlightRepository;
import com.example.demo.Reservation.Repository.ReservationRepository;
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
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight sampleFlight;

    @BeforeEach
    void setUp() {
        sampleFlight = new Flight();
        sampleFlight.setId(UUID.randomUUID());
        sampleFlight.setFlightNumber("LOT123");
        sampleFlight.setSeatNumber("12A");
        sampleFlight.setDepartureLocation("Warsaw");
        sampleFlight.setArrivalLocation("Berlin");
        sampleFlight.setFlightDuration("01:32");
        sampleFlight.setDate(LocalDate.now());
        sampleFlight.setRoundTrip(true);
    }

    @Test
    void shouldReturnAllFlightsAsDTOs() {
        when(flightRepository.findAll()).thenReturn(List.of(sampleFlight));

        List<FlightDTO> result = flightService.findAll();

        assertEquals(1, result.size());
        assertEquals("Warsaw", result.get(0).getDepartureLocation());
        verify(flightRepository).findAll();
    }

    @Test
    void shouldSaveFlight() {
        when(flightRepository.save(sampleFlight)).thenReturn(sampleFlight);

        Flight result = flightService.save(sampleFlight);

        assertNotNull(result);
        assertEquals("LOT123", result.getFlightNumber());
        verify(flightRepository).save(sampleFlight);
    }

    @Test
    void shouldDeleteFlightByFlightNumberAndSeatNumber() {
        when(flightRepository.findByFlightNumberAndSeatNumber("LOT123", "12A"))
                .thenReturn(Optional.of(sampleFlight));

        flightService.deleteByFlightNumberAndSeatNumber("LOT123", "12A");

        verify(reservationRepository).deleteAllByFlightId(sampleFlight.getId());
        verify(flightRepository).deleteByFlightNumberAndSeatNumber("LOT123", "12A");
    }

    @Test
    void shouldThrowWhenDeletingNonExistentFlight() {
        when(flightRepository.findByFlightNumberAndSeatNumber("INVALID", "XX"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            flightService.deleteByFlightNumberAndSeatNumber("INVALID", "XX");
        });

        assertTrue(ex.getMessage().contains("Flight not found"));
    }

    @Test
    void shouldFindFlightById() {
        UUID id = sampleFlight.getId();
        when(flightRepository.findById(id)).thenReturn(Optional.of(sampleFlight));

        Flight found = flightService.findById(id);

        assertEquals("LOT123", found.getFlightNumber());
        verify(flightRepository).findById(id);
    }

    @Test
    void shouldThrowWhenFlightNotFoundById() {
        UUID id = UUID.randomUUID();
        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            flightService.findById(id);
        });

        assertTrue(ex.getMessage().contains("Flight not found"));
    }

    @Test
    void shouldReturnFlightWhenFoundByFlightNumberAndSeatNumber() {
        when(flightRepository.findByFlightNumberAndSeatNumber("LOT123", "12A"))
                .thenReturn(Optional.of(sampleFlight));

        Optional<Flight> result = flightService.findByFlightNumberAndSeatNumber("LOT123", "12A");

        assertTrue(result.isPresent());
        verify(flightRepository).findByFlightNumberAndSeatNumber("LOT123", "12A");
    }

    @Test
    void shouldUpdateFlightDetails() {
        Flight updatedFlight = new Flight();
        updatedFlight.setDepartureLocation("Gdansk");
        updatedFlight.setArrivalLocation("Paris");
        updatedFlight.setFlightDuration("00:20");
        updatedFlight.setDate(LocalDate.now().plusDays(1));
        updatedFlight.setRoundTrip(false);

        when(flightRepository.findByFlightNumberAndSeatNumber("LOT123", "12A"))
                .thenReturn(Optional.of(sampleFlight));
        when(flightRepository.save(any(Flight.class))).thenReturn(sampleFlight);

        Flight result = flightService.update("LOT123", "12A", updatedFlight);

        assertEquals("Gdansk", result.getDepartureLocation());
        assertEquals("Paris", result.getArrivalLocation());
        verify(flightRepository).save(sampleFlight);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentFlight() {
        when(flightRepository.findByFlightNumberAndSeatNumber("INVALID", "XX"))
                .thenReturn(Optional.empty());

        Flight updatedFlight = new Flight();

        assertThrows(RuntimeException.class, () -> {
            flightService.update("INVALID", "XX", updatedFlight);
        });
    }
}
