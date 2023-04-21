import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import detectEthereumProvider from '@metamask/detect-provider';
import { Auth, signOut, signInWithCustomToken } from '@angular/fire/auth';
import { Observable, async, from } from 'rxjs';
import { catchError, switchMap, exhaustMap, filter } from 'rxjs/operators';
import { VerifyRequest, TokenResponse, NonceResponse, ContractFunctions } from '../model/model'
import { constants } from '../../environments/environment'

import Web3 from 'web3';

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

  public signOut() {
    return signOut(this.auth);
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
        this.walletAddress = accounts[0];
        // console.log(walletAddress)
        let url = constants.SERVER_URL + 'get-nonce'
        return this.http.post<NonceResponse>(url, { address: this.walletAddress });

      }),
      switchMap(async (response) => {
        this.nonce = response.nonce
        return await this.web3.eth.personal.sign(this.nonce, this.walletAddress, '')
      }),
      switchMap((sig) => {
        let url = constants.SERVER_URL + 'verify-signature'
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
        console.log("successful login")
      }),
      switchMap(() => {
        return this.http.get(constants.SERVER_URL + 'abi')
      }),
      switchMap((data) => {
        let a = data as { abi: string, contractAddress: string }
        this.abi = a.abi
        console.log(this.abi)
        this.contractAddress = a.contractAddress
        return ""
      }),
      catchError((err) => {
        let ok = err as Error
        return new Observable((observer) => {
          observer.next(ok.message)
          observer.complete()
        })
      })
    )
  }


  getEncodedContributeFunctionObservable() {
    let testUrl = "http://localhost:8080/contribute";
    // TODO check if user has login via metamask
    // TransactionConfig
    // this.web3.eth.getAccounts().then(this.web3.get)
    const params = new HttpParams()
      .set('address', this.walletAddress)
    return this.http.get(testUrl, { params })
    // this.web3.eth.sendSignedTransaction()
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

  getEncodedFunctionObservable(functionName: string) {
    let url = constants.SERVER_URL + functionName + 's'
    const params = new HttpParams()
      .set('functionName', functionName)
      .set('params', 100)
    this.http.get(url, { params }).subscribe((data) => {
      console.log(data)
    })
  }

  getEncodedFunctionVariableObservable(functionName: string, ...params: any[]) {

    let requestBody: any[] = []
    if (params.length != 0) {
      requestBody = params
    }
    return this.http.post(constants.SERVER_URL + "get-function-encoded/" + functionName, requestBody)
  }

  sendTransaction(encodedFunction: string) {
    // if (value) {
    //   this.web3.eth.sendTransaction({
    //     from: this.walletAddress,
    //     to: this.contractAddress,
    //     value: value,
    //     data: encodedFunction as string
    //   }).on(('sent'), (son) => {
    //     console.log(son)
    //   })
    // } else {

    // }

    // for when sending a transaction that can change the state of the blockchain
    this.web3.eth.sendTransaction({
      from: this.walletAddress,
      to: this.contractAddress,
      data: encodedFunction as string
    }).on(('error'), (error) => {
      console.log('error', error)
    }).on(('confirmation'), (c, r, blockHash) => {
      console.log(blockHash)
    })


  }

  // for when just reading the blockchain
  callContract(encodedFunction: string) {
    this.web3.eth.call({
      to: this.contractAddress,
      data: encodedFunction
    }, (error, result) => {
      if (error) {
        console.log(error)
      } else {

        console.log(this.web3.utils.hexToNumberString(result))
      }
    })
  }
}
