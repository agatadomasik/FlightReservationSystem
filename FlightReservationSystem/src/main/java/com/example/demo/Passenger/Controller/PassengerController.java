package com.example.demo.Passenger.Controller;

import com.example.demo.Passenger.Entity.Passenger;
import com.example.demo.Passenger.Entity.PassengerDTO;
import com.example.demo.Passenger.Service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/passengers")
@CrossOrigin(origins = "*")
public class PassengerController {

    private final PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping
    public ResponseEntity<List<PassengerDTO>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassengerById(@PathVariable UUID id) {
        return ResponseEntity.ok(passengerService.findById(id));
    }

    @GetMapping("/email/{email}")
    public Optional<PassengerDTO> getPassengerByEmail(@PathVariable String email) {
        return passengerService.findByEmail(email)
                .map(passenger -> {
                    PassengerDTO p = new PassengerDTO();
                    p.setLastName(passenger.getLastName());
                    p.setFirstName(passenger.getFirstName());
                    p.setEmail(passenger.getEmail());
                    p.setPhone(passenger.getPhone());
                    return p;
                });
    }



    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger) {
        Passenger saved = passengerService.save(passenger);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{email}")
    public PassengerDTO updatePassenger(
            @PathVariable String email,
            @RequestBody Passenger updatedPassenger) {

        Passenger p = passengerService.update(email, updatedPassenger);
        PassengerDTO pDTO = new PassengerDTO();
        pDTO.setEmail(p.getEmail());
        pDTO.setPhone(p.getPhone());
        pDTO.setFirstName(p.getFirstName());
        pDTO.setLastName(p.getLastName());
        return pDTO;
    }


    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deletePassengerByEmail(@PathVariable String email) {
        passengerService.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
