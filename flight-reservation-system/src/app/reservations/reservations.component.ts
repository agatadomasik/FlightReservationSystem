import { Component, OnInit } from '@angular/core';
import { ReservationService } from '../services/reservation.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {
  reservations: any[] = [];

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.reservationService.getReservations().subscribe(data => {
      this.reservations = data;
    });
  }
}
