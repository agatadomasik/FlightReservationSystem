package com.example.demo.Passenger.Service;

import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Passenger.Entity.PassengerDTO;
import com.example.demo.Passenger.Repository.PassengerRepository;
import com.example.demo.Reservation.Repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private PassengerService passengerService;

    private Passenger samplePassenger;

    @BeforeEach
    void setUp() {
        samplePassenger = new Passenger();
        samplePassenger.setId(UUID.randomUUID());
        samplePassenger.setFirstName("Jan");
        samplePassenger.setLastName("Kowalski");
        samplePassenger.setEmail("jan.kowalski@example.com");
        samplePassenger.setPhone("123456789");
    }

    @Test
    void shouldReturnAllPassengersAsDTOs() {
        when(passengerRepository.findAll()).thenReturn(List.of(samplePassenger));

        List<PassengerDTO> result = passengerService.findAll();

        assertEquals(1, result.size());
        assertEquals("Jan", result.get(0).getFirstName());
        verify(passengerRepository).findAll();
    }

    @Test
    void shouldSavePassenger() {
        when(passengerRepository.save(samplePassenger)).thenReturn(samplePassenger);

        Passenger result = passengerService.save(samplePassenger);

        assertNotNull(result);
        assertEquals("jan.kowalski@example.com", result.getEmail());
        verify(passengerRepository).save(samplePassenger);
    }

    @Test
    void shouldDeletePassengerByEmail() {
        when(passengerRepository.findByEmail("jan.kowalski@example.com"))
                .thenReturn(Optional.of(samplePassenger));

        passengerService.deleteByEmail("jan.kowalski@example.com");

        verify(reservationRepository).deleteAllByPassengerId(samplePassenger.getId());
        verify(passengerRepository).deleteByEmail("jan.kowalski@example.com");
    }

    @Test
    void shouldThrowWhenDeletingNonExistentPassenger() {
        when(passengerRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            passengerService.deleteByEmail("notfound@example.com");
        });

        assertTrue(ex.getMessage().contains("Passenger not found"));
    }

    @Test
    void shouldFindPassengerById() {
        UUID id = samplePassenger.getId();
        when(passengerRepository.findById(id)).thenReturn(Optional.of(samplePassenger));

        Passenger result = passengerService.findById(id);

        assertEquals("Jan", result.getFirstName());
        verify(passengerRepository).findById(id);
    }

    @Test
    void shouldThrowWhenPassengerNotFoundById() {
        UUID id = UUID.randomUUID();
        when(passengerRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            passengerService.findById(id);
        });

        assertTrue(ex.getMessage().contains("Passenger not found"));
    }

    @Test
    void shouldReturnPassengerWhenFoundByEmail() {
        when(passengerRepository.findByEmail("jan.kowalski@example.com"))
                .thenReturn(Optional.of(samplePassenger));

        Optional<Passenger> result = passengerService.findByEmail("jan.kowalski@example.com");

        assertTrue(result.isPresent());
        verify(passengerRepository).findByEmail("jan.kowalski@example.com");
    }

    @Test
    void shouldUpdatePassengerDetails() {
        Passenger updated = new Passenger();
        updated.setFirstName("Anna");
        updated.setLastName("Nowak");
        updated.setPhone("987654321");

        when(passengerRepository.findByEmail("jan.kowalski@example.com"))
                .thenReturn(Optional.of(samplePassenger));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(samplePassenger);

        Passenger result = passengerService.update("jan.kowalski@example.com", updated);

        assertEquals("Anna", result.getFirstName());
        assertEquals("Nowak", result.getLastName());
        assertEquals("987654321", result.getPhone());
        verify(passengerRepository).save(samplePassenger);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentPassenger() {
        when(passengerRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        Passenger updated = new Passenger();

        assertThrows(RuntimeException.class, () -> {
            passengerService.update("notfound@example.com", updated);
        });
    }
}
