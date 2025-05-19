package com.example.demo;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Flight.Service.FlightService;
import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Reservation.Entity.Reservation;
import com.example.demo.Reservation.Service.ReservationService;
import com.example.demo.Passenger.Service.PassengerService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final ReservationService reservationService;
    private final PassengerService passengerService;
    private final FlightService flightService;
    private final EmailService emailService;


    @Autowired
    public MyCommandLineRunner(ReservationService reservationService, PassengerService passengerService , FlightService flightService, EmailService emailService) {
        this.reservationService = reservationService;
        this.passengerService = passengerService;
        this.flightService = flightService;
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) throws MessagingException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Options:\n 0: all passengers\n 1: all reservations\n 2: all flights\n 3: add passenger\n " +
                    "4: add reservation\n 5: add flight\n 6: delete passenger\n 7: delete reservation\n 8: delete flight \n 9: exit");
            String command = scanner.nextLine();

            switch (command) {
                case "0":
                    passengerService.findAll().forEach(System.out::println);
                    break;

                case "1":
                    reservationService.findAll().forEach(System.out::println);
                    break;

                case "2":
                    flightService.findAll().forEach(System.out::println);
                    break;

                case "3":
                    String firstName;
                    do {
                        System.out.println("Enter first name:");
                        firstName = scanner.nextLine().trim();
                        if (!firstName.matches("[a-zA-Z]+")) {
                            System.out.println("Invalid first name. Only letters are allowed.");
                        }
                    } while (!firstName.matches("[a-zA-Z]+"));

                    String lastName;
                    do {
                        System.out.println("Enter last name:");
                        lastName = scanner.nextLine().trim();
                        if (!lastName.matches("[a-zA-Z]+")) {
                            System.out.println("Invalid last name. Only letters are allowed.");
                        }
                    } while (!lastName.matches("[a-zA-Z]+"));

                    String email;
                    do {
                        System.out.println("Enter email address:");
                        email = scanner.nextLine().trim();
                        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                            System.out.println("Invalid email format.");
                            continue;
                        }

                        if (passengerService.findByEmail(email).isPresent()) {
                            System.out.println("Passenger with this email already exists.");
                            email = null;
                        }

                    } while (email == null);

                    String phone;
                    do {
                        System.out.println("Enter phone number:");
                        phone = scanner.nextLine().trim();
                        if (!phone.matches("\\d{9,15}")) {
                            System.out.println("Invalid phone number. Only digits allowed (9–15 digits).");
                        }
                    } while (!phone.matches("\\d{9,15}"));

                    Passenger p = new Passenger.PassengerBuilder()
                            .setFirstName(firstName)
                            .setLastName(lastName)
                            .setEmail(email)
                            .setPhone(phone)
                            .build();

                    passengerService.save(p);
                    System.out.println("Passenger added successfully:\n" + p);
                    break;


                case "4":
                    String reservationNumber;
                    do {
                        System.out.println("Enter reservation number:");
                        reservationNumber = scanner.nextLine().trim();
                        if (reservationNumber.isEmpty()) {
                            System.out.println("Reservation number cannot be empty.");
                        }
                    } while (reservationNumber.isEmpty());

                    String flightNumber;
                    do {
                        System.out.println("Enter flight number:");
                        flightNumber = scanner.nextLine().trim();
                        if (flightNumber.isEmpty()) {
                            System.out.println("Flight number cannot be empty.");
                        }
                    } while (flightNumber.isEmpty());

                    String seatNumber;
                    do {
                        System.out.println("Enter seat number:");
                        seatNumber = scanner.nextLine().trim();
                        if (seatNumber.isEmpty()) {
                            System.out.println("Seat number cannot be empty.");
                        }
                    } while (seatNumber.isEmpty());

                    Optional<Flight> flightOpt = flightService.findByFlightNumberAndSeatNumber(flightNumber, seatNumber);
                    Flight flight;
                    if (flightOpt.isPresent()){
                        flight = flightOpt.get();
                        if (reservationService.existsByFlight(flight)) {
                            System.out.println("The seat '" + seatNumber + "' on the flight '" + flightNumber + "' is already taken.");
                            break;
                        }
                    } else {
                        System.out.println("Flight does not exist");
                        break;
                    }

                    String passengerEmail;
                    do {
                        System.out.println("Enter email address:");
                        passengerEmail = scanner.nextLine().trim();
                        if (!passengerEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                            System.out.println("Invalid email format.");
                            passengerEmail = "";
                        }
                    } while (passengerEmail.isEmpty());

                    Optional<Passenger> passengerOpt = passengerService.findByEmail(passengerEmail);

                    Passenger passenger;

                    if (passengerOpt.isPresent()) {
                        passenger = passengerOpt.get();
                    } else {
                        System.out.println("Passenger with email address '" + passengerEmail + "' does not exist.");
                        System.out.print("Do you want to create it? (y/n): ");
                        String answer = scanner.nextLine();

                        if (answer.equalsIgnoreCase("y")) {
                            String newFirstName;
                            do {
                                System.out.println("Enter first name:");
                                newFirstName = scanner.nextLine().trim();
                                if (!newFirstName.matches("[a-zA-Z]+")) {
                                    System.out.println("Invalid first name. Only letters are allowed.");
                                    newFirstName = "";
                                }
                            } while (newFirstName.isEmpty());

                            String newLastName;
                            do {
                                System.out.println("Enter last name:");
                                newLastName = scanner.nextLine().trim();
                                if (!newLastName.matches("[a-zA-Z]+")) {
                                    System.out.println("Invalid last name. Only letters are allowed.");
                                    newLastName = "";
                                }
                            } while (newLastName.isEmpty());

                            String newPhone;
                            do {
                                System.out.println("Enter phone number:");
                                newPhone = scanner.nextLine().trim();
                                if (!newPhone.matches("\\d{9,15}")) {
                                    System.out.println("Invalid phone number. Only digits allowed (9–15 digits).");
                                    newPhone = "";
                                }
                            } while (newPhone.isEmpty());

                            passenger = new Passenger.PassengerBuilder()
                                    .setFirstName(newFirstName)
                                    .setLastName(newLastName)
                                    .setEmail(passengerEmail)
                                    .setPhone(newPhone)
                                    .build();

                            passengerService.save(passenger);
                        } else {
                            System.out.println("Reservation cancelled.");
                            break;
                        }
                    }
                    Boolean isDeparted = null;
                    do {
                        System.out.print("Is it already departed? (true/false): ");
                        String input = scanner.nextLine().trim().toLowerCase();
                        if (input.equals("true") || input.equals("false")) {
                            isDeparted = Boolean.parseBoolean(input);
                        } else {
                            System.out.println("Invalid input. Type 'true' or 'false'.");
                        }
                    } while (isDeparted == null);

                    Reservation reservation = new Reservation.ReservationBuilder()
                            .setReservationNumber(reservationNumber)
                            .setFlight(flight)
                            .setPassenger(passenger)
                            .setIsDeparted(isDeparted)
                            .build();

                    reservationService.save(reservation);
                    System.out.println("Reservation added successfully:\n" + reservation);
                    System.out.println("Confirmation email sent to " + passenger.getEmail());
                    break;

                case "5":
                    String departure;
                    do {
                        System.out.print("Enter departure location: ");
                        departure = scanner.nextLine().trim();
                        if (!departure.matches("[a-zA-Z ]{2,50}")) {
                            System.out.println("Invalid departure location. Only letters and spaces (2-50 chars) allowed.");
                            departure = "";
                        }
                    } while (departure.isEmpty());

                    String arrival;
                    do {
                        System.out.print("Enter arrival location: ");
                        arrival = scanner.nextLine().trim();
                        if (!arrival.matches("[a-zA-Z ]{2,50}")) {
                            System.out.println("Invalid arrival location. Only letters and spaces (2-50 chars) allowed.");
                            arrival = "";
                        }
                    } while (arrival.isEmpty());

                    String duration;
                    do {
                        System.out.print("Enter flight duration (hh:mm): ");
                        duration = scanner.nextLine().trim();
                        if (!duration.matches("^\\d{1,2}:\\d{2}$")) {
                            System.out.println("Invalid duration. Format must be hh:mm.");
                            duration = "";
                        }
                    } while (duration.isEmpty());

                    String number;
                    do {
                        System.out.print("Enter flight number (e.g. F123): ");
                        number = scanner.nextLine().trim();
                        if (!number.matches("^[A-Z0-9]{2,10}$")) {
                            System.out.println("Invalid flight number. Only uppercase letters and digits allowed (2–10 chars).");
                            number = "";
                        }
                    } while (number.isEmpty());

                    Boolean roundTrip = null;
                    do {
                        System.out.print("Is it a round trip? (true/false): ");
                        String input = scanner.nextLine().trim().toLowerCase();
                        if (input.equals("true") || input.equals("false")) {
                            roundTrip = Boolean.parseBoolean(input);
                        } else {
                            System.out.println("Invalid input. Type 'true' or 'false'.");
                        }
                    } while (roundTrip == null);

                    String seat;
                    do {
                        System.out.print("Enter seat number (e.g. S1): ");
                        seat = scanner.nextLine().trim();
                        if (!seat.matches("^[A-Z0-9]{1,5}$")) {
                            System.out.println("Invalid seat number. Only uppercase letters and digits (1–5 chars).");
                            seat = "";
                        }
                    } while (seat.isEmpty());

                    Optional<Flight> existingFlight = flightService.findByFlightNumberAndSeatNumber(number, seat);
                    if (existingFlight.isPresent()) {
                        System.out.println("A flight with this flight number and seat number already exists. Cannot add.");
                        break;
                    }

                    Flight f = new Flight.FlightBuilder()
                            .setDepartureLocation(departure)
                            .setArrivalLocation(arrival)
                            .setFlightDuration(duration)
                            .setFlightNumber(number)
                            .setIsRoundTrip(roundTrip)
                            .setSeatNumber(seat)
                            .build();

                    flightService.save(f);
                    System.out.println("Flight added successfully:\n" + f);
                    break;



                case "6":
                    System.out.println("Enter passenger email to delete: ");
                    String deleteEmail = scanner.nextLine();
                    passengerService.deleteByEmail(deleteEmail);
                    System.out.println("Passenger with email '" + deleteEmail + "' deleted successfully.");
                    break;

                case "7":
                    System.out.println("Enter reservation number to delete:");
                    String deleteReservationNumber = scanner.nextLine();
                    reservationService.deleteByReservationNumber(deleteReservationNumber);
                    System.out.println("Reservation with number '" + deleteReservationNumber + "' deleted successfully.");
                    break;

                case "8":
                    System.out.println("Enter flight number to delete:");
                    String deleteFlightNumber = scanner.nextLine();
                    System.out.println("Enter seat number to delete:");
                    String deleteSeatNumber = scanner.nextLine();
                    flightService.deleteByFlightNumberAndSeatNumber(deleteFlightNumber, deleteSeatNumber);
                    System.out.println("Flight with number '" + deleteFlightNumber
                            + "' and seat number '" + deleteSeatNumber + "' deleted successfully.");
                    break;
                case "9":
                    running = false;
                    break;
            }
        }

        scanner.close();
    }
}

