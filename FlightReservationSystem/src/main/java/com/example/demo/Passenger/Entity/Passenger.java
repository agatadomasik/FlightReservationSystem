package com.example.demo.Passenger.Entity;

import com.example.demo.Reservation.Entity.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="passengers")
public class Passenger implements Comparable<Passenger>, Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "passenger", fetch = FetchType.EAGER,  cascade = CascadeType.ALL, orphanRemoval = true)
    List<Reservation> reservations;

    private Passenger(PassengerBuilder builder) {
        this.firstName =builder.firstName;
        this.lastName =builder.lastName;
        this.email =builder.email;
        this.phone =builder.phone;
        this.reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation){ reservations.add(reservation); }

    @Override
    public String toString(){
        return "{\n first name: " + firstName
                + "\n last name: " + lastName
                + "\n email address: " + email
                + "\n phone number: " + phone + "\n}";
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (other == null || other.getClass() != this.getClass()) return false;
        Passenger p = (Passenger) other;
        return firstName.equals(p.firstName) && lastName == p.lastName && reservations.equals(p.reservations);
    }

    @Override
    public int hashCode(){
        return Objects.hash(firstName, lastName);
    }

    @Override
    public int compareTo(Passenger o) {
        if (firstName.compareTo(o.firstName)!=0) return firstName.compareTo(o.firstName);
        return lastName.compareTo(o.lastName);
    }

    public static class PassengerBuilder {
        String firstName;
        String lastName;
        String email;
        String phone;
        List<Reservation> reservations;

        public PassengerBuilder setFirstName(String firstName){
            this.firstName = firstName;
            return this;
        }
        public PassengerBuilder setLastName(String lastName){
            this.lastName = lastName;
            return this;
        }
        public PassengerBuilder setEmail(String email){
            this.email = email;
            return this;
        }
        public PassengerBuilder setPhone(String phone){
            this.phone = phone;
            return this;
        }
        public PassengerBuilder setReservations(List<Reservation> reservations){
            this.reservations = reservations;
            return this;
        }

        public Passenger build(){
            return new Passenger(this);
        }
    }
}

