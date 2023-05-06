import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import detectEthereumProvider from '@metamask/detect-provider';
import { Auth, signOut, signInWithCustomToken } from '@angular/fire/auth';
import { Observable, async, from } from 'rxjs';
import { catchError, switchMap, exhaustMap, filter } from 'rxjs/operators';
import { VerifyRequest, TokenResponse, NonceResponse } from '../model/model'
import { constants } from '../../environments/environment'
import Web3 from 'web3';
import { Url } from '../util/url.util';

const AUTH_API = constants.SERVER_URL + 'api/auth/';
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

  private auth: Auth = inject(Auth);
  private nonce!: string
  web3: Web3 = new Web3(window.ethereum);
  public walletAddress!: string
  p: any
  private abi!: string
  private contractAddress!: string

  constructor(private http: HttpClient) { }

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

  register(username: string, password: string): Observable<any> {
    return this.http.post(
      AUTH_API + 'signup',
      {
        username,
        password,
      },
      httpOptions
    );
  }

  logout(): Observable<any> {
    return this.http.post(AUTH_API + 'signout', {}, httpOptions);
  }

  // firebase stuff here
  public signOut() {
    return signOut(this.auth);
  }

  public async getAccounts() {
    return await this.web3.eth.getAccounts()
  }

  public connectWallet() {
    return from(detectEthereumProvider()).pipe(
      switchMap(async (provider) => {
        this.p = provider
        if (!provider) { throw new Error('Please install MetaMask'); }
        // this.web3 = new Web3(window.ethereum);
        console.log('test')
        let obj = await window.ethereum.request({ method: 'eth_requestAccounts' });
        console.log(obj)
        window.ethereum.on('chainChanged', () => { })
        // window.ethereum.on('accountsChanged', (accounts) => { });
        return obj;
      }),
      switchMap(async () => {
        console.log('second switch map')
        return await this.web3.eth.getAccounts()
      }),
    )
  }

  public signInWithWeb3() {
    return from(detectEthereumProvider()).pipe(
      switchMap(async (provider) => {
        this.p = provider
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
        // console.log(walletAddress)
        let url = new Url()
          .add(constants.SERVER_URL)
          .add("api/")
          .add("get-nonce")
          .getUrl()
        return this.http.post<NonceResponse>(url, { address: this.walletAddress });

      }),
      switchMap(async (response) => {
        this.nonce = response.nonce
        return await this.web3.eth.personal.sign(this.nonce, this.walletAddress, '')
      }),
      switchMap((sig) => {
        // let url = constants.SERVER_URL + 'verify-signature'
        let url = new Url()
          .add(constants.SERVER_URL)
          .add("api/")
          .add("verify-signature")
          .getUrl()
        const params = new HttpParams()
          .set('nonce', this.nonce)
          .set('sig', sig)
          .set('address', this.walletAddress)
        return this.http.get(url, { params })

      }),
      switchMap(async (tokenResp) => {
        let verifiedResp = tokenResp as TokenResponse
        console.log(tokenResp)
        await signInWithCustomToken(this.auth, verifiedResp.token)
      }),
      catchError((err) => {
        let errorMessage = err as Error
        throw new Error(errorMessage.message);

        // return new Observable((observer) => {
        //   observer.next(errorMessage.message)
        //   observer.complete()
        // })
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

}
