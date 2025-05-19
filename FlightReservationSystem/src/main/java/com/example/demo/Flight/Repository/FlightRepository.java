package com.example.demo.Flight.Repository;

import com.example.demo.Flight.Entity.Flight;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {
    Optional<Flight> findByFlightNumberAndSeatNumber(String flightNumber, String seatNumber);

    @Modifying
    @Transactional
    @Query("delete from Flight f where f.flightNumber = ?1 and f.seatNumber = ?2")
    void deleteByFlightNumberAndSeatNumber(String flightNumber, String seatNumber);
}
