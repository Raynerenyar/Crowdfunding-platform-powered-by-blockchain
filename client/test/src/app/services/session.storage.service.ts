/* manages user information (e.g. username, email, roles)
 inside Browser’s Session Storage. For Logout, we will
  clear this Session Storage. */
import { Injectable } from '@angular/core';

const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {
  constructor() { }

  clean(): void {
    window.sessionStorage.clear();
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }

    return {};
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return true;
    }

    return false;
  }

  public saveAddress(address: string) {
    window.sessionStorage.removeItem("address");
    window.sessionStorage.setItem("address", address);
  }

  public getAddress(): string {
    const address = window.sessionStorage.getItem("address")
    if (address) return address
    return ""
  }

  public clearAddress() {
    window.sessionStorage.removeItem("address");
  }
}