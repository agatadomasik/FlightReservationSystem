import { Component, OnInit } from '@angular/core';
import { FlightService } from './services/flight.service';
import { ReservationService } from './services/reservation.service';
import { PassengerService } from './services/passenger.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'] // <- poprawione na "styleUrls" (zamiast styleUrl)
})
export class AppComponent implements OnInit {
  title = 'flight-reservation-system';

  flights: any[] = [];
  passengers: any[] = [];
  reservations: any[] = [];

  showAddFlightForm = false;
  showAddPassengerForm = false;
  showAddReservationForm = false;


  newFlight = {
    flightNumber: '',
    departureLocation: '',
    arrivalLocation: '',
    flightDuration: '',
    date: '',
    roundTrip: false,
    seatNumber: ''
  };

  newPassenger = {
    firstName: '',
    lastName: '',
    email: '',
    phone: ''
  };

  newReservation = {
    reservationNumber: '',
    flightNumber: '',
    seatNumber: '',
    passengerEmail: '',
    isDeparted: false
  };

  constructor(
    private flightService: FlightService,
    private reservationService: ReservationService,
    private passengerService: PassengerService
  ) {}

  ngOnInit(): void {
    this.loadFlights();
    this.loadPassengers();
    this.loadReservations();
  }

  loadFlights(): void {
    this.flightService.getFlights().subscribe(data => {
      this.flights = data;
    });
  }

  loadPassengers(): void {
    this.passengerService.getPassengers().subscribe(data => {
      this.passengers = data;
    });
  }

  loadReservations(): void {
    this.reservationService.getReservations().subscribe(data => {
      this.reservations = data;
    });
  }

  addFlight(): void {
    this.flightService.getFlightByNumberAndSeat(this.newFlight.flightNumber, this.newFlight.seatNumber).subscribe(
      (existingFlight) => {
        if (existingFlight) {
          alert(`Flight with number ${this.newFlight.flightNumber} and seat ${this.newFlight.seatNumber} already exists.`);
          return;
        }
  
        this.flightService.addFlight(this.newFlight).subscribe(() => {
          this.loadFlights();
          this.newFlight = {
            flightNumber: '',
            departureLocation: '',
            arrivalLocation: '',
            flightDuration: '',
            date: '',
            roundTrip: false,
            seatNumber: ''
          };
        });
      },
      (error) => {
        alert('Error checking if flight exists.');
      }
    );
  }
  

  deleteFlight(flightNumber: string, seatNumber: string): void {
    this.flightService.deleteFlight(flightNumber, seatNumber).subscribe(() => this.loadFlights());
  }

  addPassenger(): void {
    const isFirstNameValid = /^[a-zA-Z ]+$/.test(this.newPassenger.firstName);
    const isLastNameValid = /^[a-zA-Z ]+$/.test(this.newPassenger.lastName);
    const isEmailValid = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(this.newPassenger.email);
    const isPhoneValid = /^[0-9+]*$/.test(this.newPassenger.phone);

    if (!isFirstNameValid || !isLastNameValid || !isEmailValid || !isPhoneValid) {
      alert('Please check the input fields.');
      return;
    }

    this.passengerService.getPassengerByEmail(this.newPassenger.email).subscribe(
      (existingPassenger) => {
        if (existingPassenger) {
          alert(`Passenger with email ${this.newPassenger.email} already exists.`);
          return;
        }

        this.passengerService.addPassenger(this.newPassenger).subscribe(() => {
          this.loadPassengers();
          this.newPassenger = {
            firstName: '',
            lastName: '',
            email: '',
            phone: ''
          };
        });
      },
      (error) => {
        alert('Error checking if passenger exists.');
      }
    );
  }

  deletePassenger(email: string): void {
    this.passengerService.deletePassenger(email).subscribe(() => this.loadPassengers());
  }
  async addReservation(): Promise<void> {
    try {
      const flight = await this.flightService.getFlightByNumberAndSeat(this.newReservation.flightNumber, this.newReservation.seatNumber).toPromise();
      if (!flight || flight.length === 0) {
        alert('Flight does not exist for the given flight number and seat.');
        return;
      }
  
      const existingReservation = await this.reservationService.getReservationByNumber(this.newReservation.reservationNumber).toPromise();
      if (existingReservation) {
        alert('A reservation with this reservation number already exists.');
        return;
      }
  
      const reservationByFlightAndSeat = await this.reservationService.getReservationByFlightAndSeat(this.newReservation.flightNumber, this.newReservation.seatNumber).toPromise();
      if (reservationByFlightAndSeat) {
        alert('This seat is already reserved for this flight.');
        return;
      }
  
      const passenger = await this.passengerService.getPassengerByEmail(this.newReservation.passengerEmail).toPromise();
      if (!passenger) {
        alert(`Passenger with email ${this.newReservation.passengerEmail} does not exist.`);
        return;
      }
  
      await this.reservationService.addReservation(this.newReservation).toPromise();
      this.loadReservations();
  
      this.newReservation = {
        reservationNumber: '',
        flightNumber: '',
        seatNumber: '',
        passengerEmail: '',
        isDeparted: false
      };
    } catch (error) {
      alert('There was an error while processing the reservation. Please try again.');
    }
  }
  

  deleteReservation(reservationNumber: string): void {
    this.reservationService.deleteReservation(reservationNumber).subscribe(() => this.loadReservations());
  }


editMode = {
  flight: false,
  passenger: false,
  reservation: false
};

editingFlight: any = null;
editingPassenger: any = null;
editingReservation: any = null;

editFlight(flight: any) {
  this.editMode.flight = true;
  this.editingFlight = { ...flight };
}

updateFlight() {
  this.flightService.updateFlight(this.editingFlight).subscribe(() => {
    this.loadFlights();
    this.editMode.flight = false;
    this.editingFlight = null;
  });
}

editPassenger(passenger: any) {
  this.editMode.passenger = true;
  this.editingPassenger = { ...passenger };
}

updatePassenger() {
  this.passengerService.updatePassenger(this.editingPassenger).subscribe(() => {
    this.loadPassengers();
    this.editMode.passenger = false;
    this.editingPassenger = null;
  });
}

editReservation(reservation: any) {
  this.editMode.reservation = true;
  this.editingReservation = { ...reservation };
}

updateReservation() {
  this.reservationService.updateReservation(this.editingReservation).subscribe(() => {
    this.loadReservations();
    this.editMode.reservation = false;
    this.editingReservation = null;
  });
}

}
