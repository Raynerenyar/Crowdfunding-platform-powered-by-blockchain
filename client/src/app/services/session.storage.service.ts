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
    let address = this.getAddress()
    window.sessionStorage.clear();
    this.saveAddress(address)
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

  public saveChain(chain: string) {
    window.sessionStorage.removeItem("chain");
    window.sessionStorage.setItem("chain", chain);
  }

  public getAddress(): string {
    const address = window.sessionStorage.getItem("address")
    if (address) return address
    return ""
  }

  public getChain(): string {
    const chain = window.sessionStorage.getItem("chain")
    if (chain) return chain
    return "Unknown"
  }

  public clearAddress() {
    window.sessionStorage.removeItem("address");
  }
}