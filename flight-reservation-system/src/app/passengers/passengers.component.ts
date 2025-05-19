import { Component, OnInit } from '@angular/core';
import { PassengerService } from '../services/passenger.service';

@Component({
  selector: 'app-passengers',
  templateUrl: './passengers.component.html',
  styleUrls: ['./passengers.component.css']
})
export class PassengersComponent implements OnInit {
  passengers: any[] = [];

  constructor(private passengerService: PassengerService) {}

  ngOnInit(): void {
    this.passengerService.getPassengers().subscribe(data => {
      this.passengers = data;
    });
  }
}
