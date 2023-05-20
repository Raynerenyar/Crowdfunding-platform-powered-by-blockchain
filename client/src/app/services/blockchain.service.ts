import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { SmartContract } from '../smartContractRelated/contracts';
import Web3 from 'web3';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { constants } from 'src/environments/environment';
import { ContractTxProperties, NonceResponse } from '../model/model';
import { Url } from '../util/url.util';
import { AuthService } from './auth.service';
import { Observable, Subject, takeUntil } from 'rxjs';
import { WalletService } from './wallet.service';
import { SessionStorageService } from './session.storage.service';
import { Router } from '@angular/router';
import { PrimeMessageService } from './prime.message.service';
import { UrlBuilderService } from './url-builder.service';
import { tokenFunctionsAbi, getTransactionObject } from "../../app/smartContractRelated/contract-functons/abi.functions";
// import * as contract from "../../assets/tokenInterface/token.json";


@Injectable({
  providedIn: 'root'
})
export class BlockchainService implements OnDestroy, OnInit {

  projectAddress!: string

  web3: Web3 = new Web3(window.ethereum);

  notifier$ = new Subject<boolean>();

  constructor(private http: HttpClient, private authSvc: AuthService, private storageSvc: SessionStorageService, private router: Router, private msgSvc: PrimeMessageService, private urlBuilder: UrlBuilderService) { }
  ngOnInit(): void { }

  // project owner functions they can interact with
  createProject(goal: number, deadline: number, tokenAddress: string, title: string, description: string) {
    console.log("create project")
    return new Observable(observer => {
      let properties = SmartContract.crowdfundingFactory().createNewProject(goal, deadline, tokenAddress, title)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string, contractAddress: string }
          console.log(abiFunction.encodedFunction)
          console.log(abiFunction.contractAddress)
          // if error from field is undefined, can send error msg to tell user to login
          this.sendTransaction(abiFunction.encodedFunction, abiFunction.contractAddress)
            .on('transactionHash', async (hash) => {
              let result = await this.web3.eth.getTransactionReceipt(hash)
              console.log(result)
            }
            )
            .on(('receipt'), (receipt) => {
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              console.log(receiptCast.blockHash)

              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              let body = {
                contractName: properties.contractName,
                functionName: properties.functionName,
                blockHash: receiptCast.blockHash,
                description: description
              }

              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  observer.next()
                  console.log(data)
                })
              // this.router.navigate(['project-admin'])
            })
            .on(('error'), (error: Error) => {
              console.log(error.message)
              console.log(error.stack)
              console.log(error.cause)
              this.msgSvc.generalErrorMethod(error.message)
              observer.error()
            })
        })
    })
  }

  createRequest(projectAddress: string, title: string, description: string, recipient: string, amount: number) {
    return new Observable(observer => {
      let properties = SmartContract.crowdfunding().createRequest(title, recipient, amount)
      console.log(properties)
      this.getEncodedFunctionVariableObservable(properties, projectAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          // on getting abi encoded function & parameters
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          // get user to send transaction to crowdfunding factory contract
          this.sendTransaction(abiFunction.encodedFunction, projectAddress)
            .on(('receipt'), (receipt) => {
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              // TODO SEND REQUEST WITH PROJECT ADDRESS
              let body = {
                contractName: properties.contractName,
                functionName: properties.functionName,
                blockHash: receiptCast.blockHash,
                description: description,
                address: projectAddress
              }
              console.log(receipt)
              // post blockhash and projectAddress for backend to read events on successful creation of request
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  observer.next()
                  console.log(data)
                })
            })
            .on(('error'), (error) => {
              console.log(error)
              this.msgSvc.generalErrorMethod("error creating new request")
              observer.error()
            })
        })
    })
  }

  receiveContribution(projectAddress: string, requestNum: number): Observable<any> {
    return new Observable(observer => {

      let properties = SmartContract.crowdfunding().receiveContribution(requestNum)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, projectAddress)
            .on(('receipt'), (receipt) => {
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              // TODO SEND REQUEST WITH PROJECT ADDRESS
              let body = {
                contractName: properties.contractName,
                functionName: properties.functionName,
                blockHash: receiptCast.blockHash,
                address: projectAddress
              }
              console.log(receipt)
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  console.log(data)
                  observer.next()
                })
            })
            .on(('error'), (error) => {
              console.log(error)
              observer.error()
            })
        })
    })
  }

  // user functions they can interact with
  voteRequest(projectAddress: string, requestNum: number) {

    return new Observable(observer => {
      console.log(requestNum)
      let properties = SmartContract.crowdfunding().voteRequest(requestNum)
      this.getEncodedFunctionVariableObservable(properties, projectAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, projectAddress)
            .on(('receipt'), (receipt) => {
              // on successful vote
              observer.next()

              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              // TODO SEND REQUEST WITH PROJECT ADDRESS
              let body = {
                contractName: properties.contractName,
                functionName: properties.functionName,
                blockHash: receiptCast.blockHash,
                address: projectAddress
              }
              console.log(receipt)
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  console.log(data)
                })
            })
            .on(('error'), (error) => {
              console.log(error)
              observer.error()
            })
        })

    })
  }

  // approve token
  approveTokenSpendByContract(projectAddress: string, tokenAddress: string, walletAddress: string, amount: number) {
    return new Observable(observer => {

      let properties = SmartContract.Token().approve(projectAddress, amount)
      this.getEncodedFunctionVariableObservable(properties, tokenAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, tokenAddress)
            .on(('receipt'), (receipt) => {
              // emiting true
              observer.next()

              /* approve token has no event */

              // let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              // let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              // // TODO SEND REQUEST WITH PROJECT ADDRESS
              // let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
              // console.log(receipt)
              // // sending blockhash to server to read event
              // this.http.post(url, body)
              //   .pipe(takeUntil(this.notifier$))
              //   .subscribe((data) => {
              //     console.log(data)
              //   })
            })
            .on(('error'), (error) => {
              console.log(error)
              // emiting false
              observer.error()
            })
        })
    })
  }

  contribute(projectAddress: string, amount: number) {

    return new Observable(observer => {

      let properties = SmartContract.crowdfunding().contribute(amount)
      console.log(properties)
      this.getEncodedFunctionVariableObservable(properties, projectAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, projectAddress)
            .on(('receipt'), (receipt) => {
              // on tx success
              observer.next()
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              // TODO SEND REQUEST WITH PROJECT ADDRESS
              let body = {
                contractName: properties.contractName,
                functionName: properties.functionName,
                blockHash: receiptCast.blockHash,
                address: projectAddress
              }
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => { })
            })
            .on(('error'), (error) => {
              observer.error(error.message)
            })

        })
    })
  }

  refund(projectAddress: string) {
    return new Observable(observer => {

      let properties = SmartContract.crowdfunding().getRefund()
      this.getEncodedFunctionVariableObservable(properties, projectAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, projectAddress)
            .on(('receipt'), (receipt) => {
              // on successful refund
              observer.next()
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              // TODO SEND REQUEST WITH PROJECT ADDRESS
              let body = {
                contractName: properties.contractName,
                functionName: properties.functionName,
                blockHash: receiptCast.blockHash,
                address: projectAddress
              }
              console.log(receipt)
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  console.log(data)
                })
            })
            .on(('error'), (error) => {
              // on failure to refund
              console.log(error.message)
              console.log(error.cause)
              observer.error()
            })
        })
    })
  }

  getRaisedAmount(projectAddress: string): Observable<any> {
    return new Observable(observer => {
      let properties = SmartContract.crowdfunding().raisedAmount()
      this.getViewFunctionsReturn(properties, projectAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (resp) => {
            let value = resp as { returnFromView: string }
            observer.next(value.returnFromView)
          },
          error: (err) => {
            observer.error()
          },
        })
    })
  }

  // FAUCET
  distributeFromFaucet(): Observable<any> {
    return new Observable(observer => {
      let properties = SmartContract.DevFaucet().distribute()
      console.log(properties)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string, contractAddress: string }
          // this.web3.eth.handleRevert = true
          try {

            this.sendTransaction(abiFunction.encodedFunction, abiFunction.contractAddress)
              // .then((receipt) => {
              //   console.log(receipt)
              // })
              // .catch(error => { console.log(error) })
              .on(('receipt'), (receipt) => {
                let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
                let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
                // TODO SEND REQUEST WITH PROJECT ADDRESS
                let body = { contractName: properties.contractName, blockNumber: receiptCast.blockNumber, functionName: properties.functionName, blockHash: receiptCast.blockHash }
                console.log(receipt)
                this.http.post(url, body)
                  .pipe(takeUntil(this.notifier$))
                  .subscribe((data) => {
                    observer.next()
                  })
              })
              .on(('error'), (error: any) => {
                this.msgSvc.generalErrorMethod("You probably have claimed from the faucet less than 25 hours ago")

                console.log(this.web3.eth.handleRevert)
                console.error(error.message)
                let receiptCast = error as { blockHash: string, logs: Object[], blockNumber: number }
                let a = error.message as { blockHash: string }
                observer.error()

              })
          } catch (error) {
            observer.error()
          }
        })
    })
  }

  private getViewFunctionsReturn(properties: ContractTxProperties, contractAddress?: string) {
    let requestBody: any[] = []
    if (properties.parameters) {
      requestBody = properties.parameters
    }
    let url = this.urlBuilder
      .setPath('api/get-view-functions')
      .build()
    let httpParams;
    // somehow i cant append or set new httpParams in the 'if'
    // scope after setting it outside the 'if' scope
    if (contractAddress) {
      httpParams = new HttpParams()
        .set('contractName', properties.contractName)
        .set('functionName', properties.functionName)
        .set('contractAddress', contractAddress)
    } else {
      httpParams = new HttpParams()
        .set('contractName', properties.contractName)
        .set('functionName', properties.functionName)
    }
    return this.http.post(url, requestBody, { params: httpParams })
  }

  private getEncodedFunctionVariableObservable(properties: ContractTxProperties, contractAddress?: string): Observable<any> {
    let requestBody: any[] = []
    if (properties.parameters) {
      requestBody = properties.parameters
    }
    let url: string = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-function-encoded")
      .getUrl()
    let httpParams;
    // somehow i cant append or set new httpParams in the 'if'
    // scope after setting it outside the 'if' scope
    if (contractAddress) {
      httpParams = new HttpParams()
        .set('contractName', properties.contractName)
        .set('functionName', properties.functionName)
        .set('contractAddress', contractAddress)
    } else {
      httpParams = new HttpParams()
        .set('contractName', properties.contractName)
        .set('functionName', properties.functionName)
    }
    return this.http.post(url, requestBody, { params: httpParams })
  }

  public getBlockTimestamp() {
    return new Observable(observer => {
      this.web3.eth.getBlockNumber()
        .then((blockNum) => {
          console.log(blockNum)
          this.web3.eth.getBlock(blockNum)
            .then(
              (blockTimestamp) => {
                console.log(blockTimestamp)
                observer.next(blockTimestamp.timestamp)
              }
            )
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

  private callFunction(encodedFunction: string, projectAddress: string) {
    let address = this.storageSvc.getAddress()
    return this.web3.eth.call({
      from: address,
      to: projectAddress,
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

  public getBalanceOf(addressToCheck: string, tokenAddress: string) {
    let functionParams = [addressToCheck]
    let data = this.web3.eth.abi.encodeFunctionCall(tokenFunctionsAbi.balanceOf, functionParams)
    let transactionObj = getTransactionObject(tokenAddress, data)

    return this.web3.eth.call(transactionObj).then((result) => {
      let balance = this.web3.eth.abi.decodeParameters([tokenFunctionsAbi.balanceOf.outputs[0].type], result)
      return balance[0]
    }).catch((error) => { return error })
  }



  public async approveToken(tokenAddress: string, spenderAddress: string, fromAddress: string, amount: number) {
    let stringAmount = amount.toString()
    let functionParams = [spenderAddress, stringAmount]

    // get decimals of token
    let result = await this.getTokenDecimals(tokenAddress)
    let tokenDecimals = this.web3.eth.abi.decodeParameters([tokenFunctionsAbi.decimals.outputs[0].type], result)
    let valueString = (amount * 10 ** parseInt(tokenDecimals[0])).toString()

    console.log(valueString)

    let transactionObj = {
      to: tokenAddress,
      data: this.web3.eth.abi.encodeFunctionCall(tokenFunctionsAbi.approve, functionParams),
      from: fromAddress
    }
    // let output = this.web3.eth.abi.decodeParameters([tokenFunctionsAbi.approve.outputs[0].type], result)
    return this.web3.eth.sendTransaction(transactionObj)

  }

  private getTokenDecimals(tokenAddress: string): Promise<string> {
    let functionParams: string[] = []
    let transactionObj = {
      to: tokenAddress,
      data: this.web3.eth.abi.encodeFunctionCall(tokenFunctionsAbi.decimals, functionParams)
    }
    let returnResult: { [key: string]: any; }
    return this.web3.eth.call(transactionObj, (error, result) => {
      if (error) {
        console.log(error)
      } else {
        returnResult = this.web3.eth.abi.decodeParameters([tokenFunctionsAbi.decimals.outputs[0].type], result)
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

  ngOnDestroy(): void {
    this.notifier$.next(true);
    this.notifier$.unsubscribe()
  }


}
