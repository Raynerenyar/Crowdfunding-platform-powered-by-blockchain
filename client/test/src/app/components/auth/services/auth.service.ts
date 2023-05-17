/* This service sends registration, login,
logout HTTP POST requests to back-end.
It provides following important functions: */
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject, takeUntil, timer } from 'rxjs';
import { constants } from '../../../../environments/environment';

const AUTH_API = constants.SERVER_URL + 'api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Credentials': 'true' })
};


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) { }

  notifier$ = new Subject<boolean>()
  login(username: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signin',
      {
        username,
        password,
      },
      httpOptions
    );
  }

  register(username: string, email: string, password: string): Observable<any> {
    console.log("registering")
    return this.http.post(
      AUTH_API + 'signup',
      {
        username,
        email,
        password,
      },
      httpOptions
    );
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + 'signout', {}, httpOptions);
  }

  reload() {
    timer(3000).pipe(takeUntil(this.notifier$)).subscribe(t => {
      window.location.reload();
    })
  }
}
