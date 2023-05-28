import { Injectable, OnDestroy, OnInit } from '@angular/core';
import Web3 from 'web3';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { constants } from 'src/environments/environment';
import { ContractTxProperties, NonceResponse, EncodedFunction } from '../model/model';
import { Url } from '../util/url.util';
import { AuthService } from './auth.service';
import { Observable, Subject, Subscription, takeUntil } from 'rxjs';
import { WalletService } from './wallet.service';
import { SessionStorageService } from './session.storage.service';
import { Router } from '@angular/router';
import { PrimeMessageService } from './prime.message.service';
import { UrlBuilderService } from './url-builder.service';
import { tokenFunctionsAbi, getTransactionObject } from "../util/abi.functions";
import BN from 'bn.js';

@Injectable({
  providedIn: 'root'
})
/*  - To send transaction, first get the function encoded from the server
*     upon receiving it, send transaction with the encoded function.
*     Same for view functions.
*
*   - Interactions with smart contract can be done from the client side but exposes client to smart contract ABI
*     that may be private.
*   - All interactions with own smart contract was done this way for learning and practice.
*   - Some transactions or view functions were done on the client side sucn as
*     approving token spend and getting token balance for learning and practice
*   - Although not having exposed ABI or smart contract source code does not guarantee zero exploitation
*     It just makes it slightly more tedious as the bytecode on the blockchain has to be decoded.
*/
export class BlockchainService {

  projectAddress!: string

  web3: Web3 = new Web3(window.ethereum);
  postSubscription$!: Subscription
  subArr: Subscription[] = []

  // notifier$ = new Subject<boolean>();
  readEventEndpoint!: string

  constructor(
    private http: HttpClient,
    private authSvc: AuthService,
    private storageSvc: SessionStorageService,
    private router: Router,
    private msgSvc: PrimeMessageService,
    private urlBuilder: UrlBuilderService,
    private walletSvc: WalletService
  ) { this.readEventEndpoint = this.urlBuilder.setPath("api/read-event").build() }

  createProject(goal: number, deadline: number, tokenAddress: string, title: string, description: string, imageUrl: string) {
    console.log("create project")
    return new Observable(observer => {
      let url = this.urlBuilder.setPath("api/factory/project").build()
      let subscription$ = new Subscription

      // post to server to get encoded abi function
      subscription$ = this.http.post(url, { goal: goal, deadline: deadline, tokenAddress: tokenAddress, title: title, imageUrl: imageUrl })
        .pipe()
        .subscribe((resp) => {
          let encodedFunction = resp as EncodedFunction;

          // send transaction through metamask
          this.sendTransaction(encodedFunction.encodedFunction, encodedFunction.contractAddress)
            .on(('receipt'), (receipt) => {
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let body = {
                contractName: "CrowdfundingFactory",
                functionName: "createNewProject",
                blockHash: receiptCast.blockHash,
                description: description,
                imageUrl: imageUrl
              }
              let subscription$ = new Subscription

              // post to server to retrieve events
              subscription$ = this.http.post(this.readEventEndpoint, body)
                .pipe()
                .subscribe(() => {
                  observer.next()
                })
              this.subArr.push(subscription$)
            })
            .on(('error'), (error) => observer.error(error))
        })
      this.subArr.push(subscription$)
    })
  }

  createRequest(projectAddress: string, title: string, description: string, recipient: string, amount: number): Observable<any> {
    return new Observable(observer => {
      let url = this.urlBuilder.setPath("api/crowdfunding/transaction/request").build()
      let params = new HttpParams()
        .set('projectAddress', projectAddress)
      let subscription$ = new Subscription

      // post to server to get encoded abi function
      subscription$ = this.http.post(url, { title, recipient, amount }, { params })
        .pipe()
        .subscribe({
          next: (resp) => {
            let encodedFunction = resp as EncodedFunction

            // send transaction through metamask
            this.sendTransaction(encodedFunction.encodedFunction, projectAddress)
              .on(('receipt'), (receipt) => {
                let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
                let body = {
                  contractName: "Crowdfunding",
                  functionName: "createRequest",
                  blockHash: receiptCast.blockHash,
                  description: description,
                  address: projectAddress
                }
                console.log(receipt)
                let subscription$ = new Subscription

                // post to server to retrieve blockchain event
                subscription$ = this.http.post(this.readEventEndpoint, body)
                  .pipe()
                  .subscribe((data) => {
                    observer.next()
                    console.log(data)
                  })
                this.subArr.push(subscription$)
              })
              .on(('error'), (error) => {
                this.msgSvc.generalErrorMethod("error creating new request")
                observer.error(error)
              })
          },
          error: (err) => {
            observer.error(err)
          },
        })
      this.subArr.push(subscription$)
    })
  }

  contribute(projectAddress: string, amount: number) {
    return new Observable(observer => {
      let url = this.urlBuilder.setPath("api/crowdfunding/transaction/contribute").build()
      let params = new HttpParams()
        .set('projectAddress', projectAddress)
      let subscription$ = new Subscription

      // post to server to get encoded abi function
      subscription$ = this.http.post(url, { amount }, { params })
        .pipe()
        .subscribe({
          next: (resp) => {
            let encodedFunction = resp as EncodedFunction

            // send transaction through metamask
            this.sendTransaction(encodedFunction.encodedFunction, encodedFunction.contractAddress)
              .on(('receipt'), (receipt) => {
                let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
                let body = {
                  contractName: "Crowdfunding",
                  functionName: "contribute",
                  blockHash: receiptCast.blockHash,
                  address: projectAddress
                }
                let subscription$ = new Subscription

                // post to server to read events
                subscription$ = this.http.post(this.readEventEndpoint, body)
                  .pipe()
                  .subscribe(() => observer.next())
                this.subArr.push(subscription$)
              })
              .on(('error'), (error) => observer.error())
          },
          error: (err) => {
            observer.error()
          },
        })
      this.subArr.push(subscription$)

    })
  }

  receiveContribution(projectAddress: string, requestNum: number): Observable<any> {
    return new Observable(observer => {
      let url = this.urlBuilder.setPath("api/crowdfunding/transaction/receive/contribution").build()
      let params = new HttpParams()
        .set('projectAddress', projectAddress)
      let subscription$ = new Subscription

      // post to server to get encoded abi function
      subscription$ = this.http.post(url, { requestNum }, { params })
        .pipe()
        .subscribe({
          next: (resp) => {
            let encodedFunction = resp as EncodedFunction

            // send transaction through metamask
            this.sendTransaction(encodedFunction.encodedFunction, encodedFunction.contractAddress)
              .on(('receipt'), (receipt) => {
                let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
                let body = {
                  contractName: "Crowdfunding",
                  functionName: "receiveContribution",
                  blockHash: receiptCast.blockHash,
                  address: projectAddress
                }
                let subscription$ = new Subscription

                // post to server to read events
                subscription$ = this.http.post(this.readEventEndpoint, body)
                  .pipe()
                  .subscribe(() => observer.next())
                this.subArr.push(subscription$)
              })
              .on(('error'), (error) => observer.error())
          },
          error: () => observer.error()
        })
      this.subArr.push(subscription$)
    })
  }

  voteRequest(projectAddress: string, requestNum: number): Observable<any> {
    return new Observable(observer => {
      let url = this.urlBuilder.setPath("api/crowdfunding/transaction/vote").build()
      let params = new HttpParams()
        .set('projectAddress', projectAddress)
      let subscription$ = new Subscription

      // post to server to get encoded abi function
      subscription$ = this.http.post(url, { requestNum }, { params })
        .pipe()
        .subscribe({
          next: (resp) => {
            let encodedFunction = resp as EncodedFunction

            // send transaction through metamask
            this.sendTransaction(encodedFunction.encodedFunction, encodedFunction.contractAddress)
              .on(('receipt'), (receipt) => {
                let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
                let body = {
                  contractName: "Crowdfunding",
                  functionName: "voteRequest",
                  blockHash: receiptCast.blockHash,
                  address: projectAddress
                }
                let subscription$ = new Subscription

                // post to server to retrieve events
                subscription$ = this.http.post(this.readEventEndpoint, body)
                  .pipe()
                  .subscribe(() => observer.next())
                this.subArr.push(subscription$)
              })
              .on(('error'), (error: any) => observer.error(error))
          },
          error: (err) => observer.error(),
        })
      this.subArr.push(subscription$)

    })
  }

  getRefund(projectAddress: string) {
    return new Observable(observer => {

      if (this.walletSvc.isOnRightChain()) {

        let url = this.urlBuilder.setPath("api/crowdfunding/transaction/refund").build()
        let params = new HttpParams()
          .set('projectAddress', projectAddress)
        let subscription$ = new Subscription

        // post to server to get encoded abi function
        subscription$ = this.http.get(url, { params })
          .pipe()
          .subscribe({
            next: (resp) => {
              let encodedFunction = resp as EncodedFunction

              // send transaction through metamask
              this.sendTransaction(encodedFunction.encodedFunction, encodedFunction.contractAddress)
                .on(('receipt'), (receipt) => {
                  let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
                  let body = {
                    contractName: "Crowdfunding",
                    functionName: "getRefund",
                    blockHash: receiptCast.blockHash,
                    address: projectAddress
                  }
                  let subscription$ = new Subscription

                  // post to server to read events
                  subscription$ = this.http.post(this.readEventEndpoint, body)
                    .pipe()
                    .subscribe({ next: () => observer.next() })
                  this.subArr.push(subscription$)

                })
                .on(('error'), (error: any) => observer.error())
            },
            error: (err) => observer.error(),
          })
        this.subArr.push(subscription$)
      } else {
        this.msgSvc.tellToConnectToChain()
        observer.error()
      }

    })
  }

  getContributionAmount(projectAddress: string, contributorAddress: string, tokenAddress: string): Observable<any> {
    return new Observable(observer => {
      if (this.walletSvc.isOnRightChain()) {

        let url = this.urlBuilder.setPath("api/crowdfunding/view/contribute/amount").build()
        let params = new HttpParams()
          .set('projectAddress', projectAddress)
        let body = {
          contributorAddress: contributorAddress
        }
        let subscription$ = new Subscription

        // post to server for value
        subscription$ = this.http.post(url, body, { params })
          .pipe()
          .subscribe({
            next: (amount) => {
              let amountNum = amount as number
              observer.next(amountNum)
            },
            error: (err) => observer.error(err),
          })
        this.subArr.push(subscription$)

      } else {
        this.msgSvc.tellToConnectToChain()
        observer.error()
      }

    })
  }

  getRaisedAmount(projectAddress: string, tokenAddress: string): Observable<any> {
    return new Observable(observer => {
      if (this.walletSvc.isOnRightChain()) {
        let url = this.urlBuilder.setPath("api/crowdfunding/view/raised/amount").build()
        let params = new HttpParams()
          .set('projectAddress', projectAddress)
        let subscription$ = new Subscription

        // post to server for value
        subscription$ = this.http.get(url, { params })
          .pipe()
          .subscribe({
            next: (amount) => {
              let amountNum = amount as number
              observer.next(amountNum)
            },
            error: (err) => {
              observer.error(err)
            },
          })
        this.subArr.push(subscription$)
      } else {
        this.msgSvc.tellToConnectToChain()
        observer.error()
      }
    })
  }

  // FAUCET
  distributeFromFaucet(): Observable<any> {
    return new Observable(observer => {
      let url = this.urlBuilder.setPath("api/faucet/distribute").build()
      let subscription$ = new Subscription

      // post to server to get encoded abi function
      subscription$ = this.http.get(url)
        .pipe()
        .subscribe({
          next: (resp) => {
            let encodedFunction = resp as EncodedFunction;

            // send transaction through metamask
            this.sendTransaction(encodedFunction.encodedFunction, encodedFunction.contractAddress)
              .on(('receipt'), (receipt) => {
                let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
                let body = {
                  contractName: "DevFaucet",
                  functionName: "distribute",
                  blockNumber: receiptCast.blockNumber,
                  blockHash: receiptCast.blockHash
                }
                let subscription$ = new Subscription

                // post to server to read events
                subscription$ = this.http.post(this.readEventEndpoint, body)
                  .pipe()
                  .subscribe({
                    next: () => {
                      observer.next()
                    }
                  })
                this.subArr.push(subscription$)

              })
              .on(('error'), (error: any) => observer.error())
          },
          error: (err) => {
            observer.error()
          },
        })
      this.subArr.push(subscription$)

    })
  }

  public getBlockTimestamp(): Observable<any> {
    return new Observable(observer => {

      this.web3.eth.getBlockNumber()
        .then((blockNum) => {
          this.web3.eth.getBlock(blockNum)
            .then(
              (blockTimestamp) => {
                let blockTimestampSeconds = blockTimestamp.timestamp as number
                let blockTimestampMiliSeconds = blockTimestampSeconds * 1000
                observer.next(blockTimestampMiliSeconds)
              })
            .catch((error) => {
              console.log(error)
              observer.error(error)
            })
        })
        .catch((error) => {
          console.log(error)
          observer.error(error)
        })
    })
  }

  private sendTransaction(encodedFunction: string, contractAddress: string) {
    this.web3.eth.handleRevert = true
    let address = this.storageSvc.getAddress()
    return this.web3.eth.sendTransaction({
      from: address as string,
      to: contractAddress as string,
      data: encodedFunction as string
    })
  }

  public getTokenSymbol(TokenAddress: string) {
    let abi = tokenFunctionsAbi.symbol
    let functionParams: string[] = []
    let data = this.web3.eth.abi.encodeFunctionCall(abi, functionParams)
    let transactionObj = getTransactionObject(TokenAddress, data)
    return this.web3.eth.call(transactionObj).then((result) => {
      let symbol = this.web3.eth.abi.decodeParameters([abi.outputs[0].type], result)
      return symbol[0]
    }).catch((error) => { return error })
  }

  public getBalanceOf(addressToCheck: string, tokenAddress: string): Promise<any> {
    let functionParams = [addressToCheck]
    let data = this.web3.eth.abi.encodeFunctionCall(tokenFunctionsAbi.balanceOf, functionParams)
    let transactionObj = getTransactionObject(tokenAddress, data)
    return new Promise((resolve, reject) => {
      try {
        this.web3.eth.call(transactionObj)
          .then(async (result) => {
            let balance
            balance = this.web3.eth.abi.decodeParameters([tokenFunctionsAbi.balanceOf.outputs[0].type], result)
            this.getTokenDecimals(tokenAddress)
            let valueBN = new BN(balance[0])
            let decimals = await this.getTokenDecimals(tokenAddress)
            let decimalsBN = new BN(decimals)
            let value = valueBN.div(new BN(10).pow(decimalsBN)).toNumber()
            resolve(value)
          })
          .catch((error) => { reject(error) })
      } catch (error) { reject(error) }
    })
  }



  public async approveToken(tokenAddress: string, spenderAddress: string, fromAddress: string, amount: number) {
    console.log("approving token")
    // get decimals of token
    let decimals = await this.getTokenDecimals(tokenAddress)
    console.log(decimals)
    let amountBN = new BN(amount)
    let tenBN = new BN(10)
    let decimalsBN = new BN(decimals)
    let valueString = amountBN.mul(tenBN.pow(decimalsBN)).toString()

    let functionParams = [spenderAddress, valueString]
    console.log(valueString, functionParams)
    let transactionObj = {
      to: tokenAddress,
      data: this.web3.eth.abi.encodeFunctionCall(tokenFunctionsAbi.approve, functionParams),
      from: fromAddress
    }
    return this.web3.eth.sendTransaction(transactionObj)

  }

  private getTokenDecimals(tokenAddress: string): Promise<string> {
    let functionParams: string[] = []
    let data = this.web3.eth.abi.encodeFunctionCall(tokenFunctionsAbi.decimals, functionParams)
    let transactionObj = getTransactionObject(tokenAddress, data)
    let returnResult: { [key: string]: any; }
    return new Promise(async (resolve, reject) => {
      try {
        let result = await this.web3.eth.call(transactionObj)
        returnResult = this.web3.eth.abi.decodeParameters([tokenFunctionsAbi.decimals.outputs[0].type], result)
        resolve(returnResult[0])
      } catch (error) {
        // defaults to 18 decimals if error
        reject("18")
      }
    })
  }

  public toChecksumAddress(address: string): string {
    return this.web3.utils.toChecksumAddress(address)
  }

  public checkCheckSumAddress(address: string): boolean {
    let modifiedAddress = this.toChecksumAddress(address)
    return this.web3.utils.checkAddressChecksum(modifiedAddress)
  }

  /* this already check for checksum */
  public isAddress(address: string): boolean {
    return this.web3.utils.isAddress(address)
  }

  // manually clean up service
  cleanUp() {
    this.subArr.forEach(sub => {
      console.log("sub getting unsub")
      sub.unsubscribe()
    });
    this.subArr = []
  }

}
