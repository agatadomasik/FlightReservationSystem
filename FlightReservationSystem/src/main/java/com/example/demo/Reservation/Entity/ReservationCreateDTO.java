package com.example.demo.Reservation.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationCreateDTO {
    private String reservationNumber;
    private String flightNumber;
    private String seatNumber;
    private String passengerEmail;
    private Boolean isDeparted;
}
