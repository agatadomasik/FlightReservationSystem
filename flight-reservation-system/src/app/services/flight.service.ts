import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlightService {
  private apiUrl = 'http://localhost:8080/api/flights'; // backend URL

  constructor(private http: HttpClient) {}

  getFlights(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
  getFlightByNumberAndSeat(flightNumber: string, seatNumber: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${flightNumber}/${seatNumber}`);
  }
  addFlight(flight: any): Observable<any> {
    return this.http.post(this.apiUrl, flight);
  }
  deleteFlight(flightNumber: string, seatNumber: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}?flightNumber=${flightNumber}&seatNumber=${seatNumber}`);
  }
  updateFlight(flight: any) {
    return this.http.put(`${this.apiUrl}/${flight.flightNumber}/${flight.seatNumber}`, flight);
  }
  
}