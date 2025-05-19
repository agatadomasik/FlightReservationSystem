package com.example.demo.Passenger.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PassengerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public PassengerDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public String toString(){
        return "{\n first name: " + firstName
                + "\n last name: " + lastName
                + "\n email address: " + email
                + "\n phone number: " + phone + "\n}";
    }
}
