package com.example.demo.Flight.Controller;

import com.example.demo.Flight.Entity.Flight;
import com.example.demo.Flight.Entity.FlightDTO;
import com.example.demo.Flight.Service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<FlightDTO> getAllFlights() {
        return flightService.findAll();
    }

    @PostMapping
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.save(flight);
    }

    @PutMapping("/{flightNumber}/{seatNumber}")
    public FlightDTO updateFlight(
            @PathVariable String flightNumber,
            @PathVariable String seatNumber,
            @RequestBody Flight flight) {

        Flight f = flightService.update(flightNumber, seatNumber, flight);
        FlightDTO flightDTO = convertToDTO(f);

        return flightDTO;
    }

    @DeleteMapping
    public void deleteFlight(@RequestParam String flightNumber, @RequestParam String seatNumber) {
        flightService.deleteByFlightNumberAndSeatNumber(flightNumber, seatNumber);
    }

    @GetMapping("/{id}")
    public FlightDTO getFlightById(@PathVariable UUID id) {
        Flight flight = flightService.findById(id);
        return convertToDTO(flight);
    }

    @GetMapping("/{flightNumber}/{seatNumber}")
    public Optional<FlightDTO> getFlightByNumberAndSeat(@PathVariable String flightNumber, @PathVariable String seatNumber) {
        return flightService.findByFlightNumberAndSeatNumber(flightNumber, seatNumber)
                .map(flight -> new FlightDTO(
                        flight.getDepartureLocation(),
                        flight.getArrivalLocation(),
                        flight.getFlightDuration(),
                        flight.getDate(),
                        flight.getFlightNumber(),
                        flight.isRoundTrip(),
                        flight.getSeatNumber()
                ));
    }


    private FlightDTO convertToDTO(Flight flight) {
        return new FlightDTO(
                flight.getDepartureLocation(),
                flight.getArrivalLocation(),
                flight.getFlightDuration(),
                flight.getDate(),
                flight.getFlightNumber(),
                flight.isRoundTrip(),
                flight.getSeatNumber()
        );
    }
}
