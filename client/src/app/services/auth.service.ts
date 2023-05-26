import { Injectable, OnDestroy, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import detectEthereumProvider from '@metamask/detect-provider';
import { Observable, Subject, Subscription, async, from, timer } from 'rxjs';
import { catchError, switchMap, exhaustMap, filter, takeUntil } from 'rxjs/operators';
import { VerifyRequest, TokenResponse, NonceResponse } from '../model/model'
import { constants } from '../../environments/environment'
import Web3 from 'web3';
import { Url } from '../util/url.util';
import { UrlBuilderService } from './url-builder.service';
import { PrimeMessageService } from './prime.message.service';

const AUTH_API = `${constants.SERVER_URL}/api/auth/`;
// 'Access-Control-Allow-Credentials': 'true' }
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

declare global {
  interface Window {
    ethereum: any;
  }
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  nonce!: string
  web3: Web3 = new Web3(window.ethereum);
  public walletAddress!: string
  private abi!: string
  private contractAddress!: string
  subArr: Subscription[] = []

  constructor(private http: HttpClient, private urlBuilder: UrlBuilderService, private msgSvc: PrimeMessageService) { }

  login(username: string, password: string, signed?: string, nonce?: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signin',
      {
        username,
        password,
        signed,
        nonce
      },
      httpOptions
    );
  }

  register(username: string, password: string, signed?: string, nonce?: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signup',
      {
        username,
        password,
        signed,
        nonce
      },
      httpOptions
    );
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + 'signout', {}, httpOptions);
  }

  public getSigned() {
    return from(detectEthereumProvider()).pipe(
      switchMap(async (provider) => {
        if (!provider) { throw new Error('Please install MetaMask'); }
        // this.web3 = new Web3(window.ethereum);
        return await window.ethereum.request({ method: 'eth_requestAccounts' });
      }),
      switchMap(async () => {
        return await this.web3.eth.getAccounts()
      }),
      switchMap((resp) => {
        let accounts = resp as string[]
        console.log(resp)
        this.walletAddress = accounts[0];
        let url = this.urlBuilder.setPath("api/auth/get-nonce").build()
        return this.http.post<NonceResponse>(url, { address: this.walletAddress });

      }),
      switchMap(async (response) => {
        this.nonce = response.nonce
        return await this.web3.eth.personal.sign(this.nonce, this.walletAddress, '')
      }),
      catchError((err) => {
        let errorMessage = err as Error
        throw new Error(errorMessage.message);
      })
    )
  }

  async requestApproval(encodedTransaction: Object) {
    console.log("requestint approval", encodedTransaction)
    // let contract = await new this.web3.eth.Contract(JSON.parse(this.abi), this.contractAddress)
    await this.web3.eth.getAccounts().then((accounts) => {
      this.walletAddress = accounts[0]
      this.contractAddress = '0x278430111B01eb0CAa639F693c4fd2Ea3aa069FC'
    })
    this.web3.eth.sendTransaction({
      from: this.walletAddress,
      to: this.contractAddress,
      value: 100,
      data: encodedTransaction as string
    }).on(('sent'), (son) => {
      console.log(son)
    })
  }

  reloadPage(): void {
    let subscription$ = new Subscription

    subscription$ = timer(3000)
      .pipe()
      .subscribe(t => {
        window.location.reload()
      })
    this.subArr.push(subscription$)

  }

  cleanUp() {
    this.subArr.forEach(sub => {
      console.log("sub getting unsub")
      sub.unsubscribe()
    });
    this.subArr = []
  }

}
