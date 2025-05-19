import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FlightsComponent } from './flights/flights.component';
import { PassengersComponent } from './passengers/passengers.component';
import { ReservationsComponent } from './reservations/reservations.component';

const routes: Routes = [
  { path: 'flights', component: FlightsComponent },
  { path: 'passengers', component: PassengersComponent },
  { path: 'reservations', component: ReservationsComponent },
  { path: '', redirectTo: '/flights', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
