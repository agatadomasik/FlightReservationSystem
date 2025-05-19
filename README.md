# Flight Reservation System

## Opis
Prosty system do zarządzania rezerwacjami lotów napisany w Javie ze Spring Boot. Umożliwia CRUD dla lotów, pasażerów i rezerwacji, zawiera walidację danych, wysyłkę e-maili z potwierdzeniem oraz testy jednostkowe. Frontend w Angularze, baza H2 w trybie in-memory.

## Technologie
- **Backend:** Java 21, Spring Boot 3.3.4, Spring Data JPA, Spring Mail, H2, Lombok, Maven  
- **Frontend:** Angular 17  
- **Testy:** JUnit 5, Mockito  

## Funkcjonalności
- CRUD lotów, pasażerów i rezerwacji  
- Walidacja danych i dostępności miejsc  
- Automatyczne wysyłanie e-maili z potwierdzeniem  
- Konsolowa wersja aplikacji (CommandLineRunner)  

## Model danych
- **Flight:** numer lotu, miejsce wylotu/przylotu, data, czas trwania, miejsce  
- **Passenger:** imię, nazwisko, unikalny e-mail, telefon  
- **Reservation:** unikalny numer, lot, miejsce, dane pasażera, status wylotu  
