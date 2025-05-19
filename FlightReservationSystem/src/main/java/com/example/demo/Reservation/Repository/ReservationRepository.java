package com.example.demo.Reservation.Repository;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Reservation.Entity.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findByReservationNumber(String reservationNumber);

    @Modifying
    @Transactional
    @Query("delete from Reservation r where r.passenger.id = :passengerId")
    void deleteAllByPassengerId(
            @Param("passengerId") UUID passengerId
    );

    @Modifying
    @Transactional
    @Query("delete from Reservation r where r.flight.id = :flightId")
    void deleteAllByFlightId(
            @Param("flightId") UUID flightId
    );


    @Modifying
    @Transactional
    @Query("delete from Reservation r where r.reservationNumber = :reservationNumber")
    void deleteByReservationNumber(
            @Param("reservationNumber") String reservationNumber
    );

    @Modifying
    @Transactional
    @Query("delete from Reservation r where r.id = :id")
    void deleteById(
            @Param("id") UUID id
    );


    List<Reservation> findByPassengerEmail(String passengerEmail);
    boolean existsByFlight(Flight flight);

    Optional<Reservation> findByFlight(Flight flight);
}
