package com.example.demo.Reservation.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("LombokSetterMayBeUsed")
@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
    private String reservationNumber;
    private String flightNumber;
    private String seatNumber;
    private String passengerFirstName;
    private String passengerLastName;
    private String passengerEmail;
    private String passengerPhone;
    private boolean isDeparted;

    public ReservationDTO(String reservationNumber, String flightNumber, String seatNumber,
                          String passengerFirstName, String passengerLastName, String passengerEmail, String passengerPhone, boolean isDeparted) {
        this.reservationNumber = reservationNumber;
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.passengerFirstName = passengerFirstName;
        this.passengerLastName = passengerLastName;
        this.passengerEmail = passengerEmail;
        this.passengerPhone = passengerPhone;
        this.isDeparted = isDeparted;
    }


    @Override
    public String toString() {
        return "{\n reservation number: " + reservationNumber
                + "\n flight number: " + flightNumber
                + "\n seat number: " + seatNumber
                + "\n passenger first name: " + passengerFirstName
                + "\n passenger last name: " + passengerLastName
                + "\n passenger email address: " + passengerEmail
                + "\n passenger phone number: " + passengerPhone
                + "\n is it departed: " + isDeparted + "\n}";
    }

    public boolean getIsDeparted() {
        return isDeparted;
    }
    public void setIsDeparted(boolean isDeparted) {
        this.isDeparted = isDeparted;
    }
}
