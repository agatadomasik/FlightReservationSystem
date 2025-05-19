# Flight Reservation System

## Description
A simple flight reservation management system written in Java using Spring Boot. Supports CRUD operations for flights, passengers, and reservations. Includes data validation, email confirmation sending, and unit tests. Frontend built with Angular, using H2 in-memory database.

## Technologies
- **Backend:** Java 21, Spring Boot 3.3.4, Spring Data JPA, Spring Mail, H2, Lombok, Maven  
- **Frontend:** Angular 17  
- **Testing:** JUnit 5, Mockito  

## Features
- CRUD for flights, passengers, and reservations  
- Data validation and seat availability checks  
- Automatic email confirmation after booking  
- Console version of the app (CommandLineRunner)  

## Data Model
- **Flight:** flight number, departure/arrival locations, date, duration, seat number  
- **Passenger:** first name, last name, unique email, phone  
- **Reservation:** unique reservation number, flight, seat, passenger details, departure status  
