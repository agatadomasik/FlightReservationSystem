import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient) {}

  getReservations(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
  addReservation(reservation: any): Observable<any> {
    return this.http.post(this.apiUrl, reservation);
  }
  getReservationByFlightAndSeat(flightNumber: string, seatNumber: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/flight/${flightNumber}/seat/${seatNumber}`);
  }
  getReservationByNumber(reservationNumber: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/number/${reservationNumber}`);
  }
  deleteReservation(reservationNumber: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}?reservationNumber=${reservationNumber}`);
  }
  updateReservation(reservation: any) {
    return this.http.put(`${this.apiUrl}/${reservation.reservationNumber}`, reservation);
  }
}
