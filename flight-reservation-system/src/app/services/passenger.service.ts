import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PassengerService {
  private apiUrl = 'http://localhost:8080/api/passengers';

  constructor(private http: HttpClient) {}

  getPassengers(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
  getPassengerByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/email/${email}`);
  }
  addPassenger(passenger: any): Observable<any> {
    return this.http.post(this.apiUrl, passenger);
  }
  deletePassenger(email: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/email/${email}`);
  }
  updatePassenger(passenger: any) {
    return this.http.put(`${this.apiUrl}/${passenger.email}`, passenger);
  }
}