package com.example.demo.Passenger.Repository;

import com.example.demo.Passenger.Entity.Passenger;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
    Optional<Passenger> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("delete from Passenger p where p.email = ?1")
    void deleteByEmail(String email);
}
