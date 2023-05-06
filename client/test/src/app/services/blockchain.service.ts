import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { SmartContract } from '../smartContractRelated/contracts';
import Web3 from 'web3';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { constants } from 'src/environments/environment';
import { ContractTxProperties, NonceResponse } from '../model/model';
import { Url } from '../util/url.util';
import { AuthService } from './auth.service';
import { Observable, Subject, takeUntil } from 'rxjs';
import { WalletService } from './wallet.service';
import { StorageService } from './storage.service';
import { Router } from '@angular/router';
import { AlertMessageService } from './alert.message.service';
// import * as contract from "../../assets/tokenInterface/token.json";

@Injectable({
  providedIn: 'root'
})
export class BlockchainService implements OnDestroy, OnInit {

  projectAddress!: string

  web3: Web3 = new Web3(window.ethereum);

  notifier$ = new Subject<boolean>();

  constructor(private http: HttpClient, private authSvc: AuthService, private storageSvc: StorageService, private router: Router, private msgSvc: AlertMessageService) { }
  ngOnInit(): void {
    this.web3.eth.handleRevert = true;
  }

  // project owner functions they can interact with
  createProject(goal: number, deadline: number, tokenAddress: string, title: string, description: string) {
    return new Observable(observer => {
      let properties = SmartContract.crowdfundingFactory().createNewProject(goal, deadline, tokenAddress, title)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          // if error from field is undefined, can send error msg to tell user to login
          this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
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
            .on(('error'), (error) => {
              this.msgSvc.generalErrorMethod(error.message)
            })
        })
    })
  }

  createRequest(address: string, title: string, description: string, recipient: string, amount: number) {
    return new Observable(observer => {
      let properties = SmartContract.crowdfundingFactory().createRequestForProject(address, title, recipient, amount)
      console.log(properties)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
            .on(('receipt'), (receipt) => {
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              let body = {
                contractName: properties.contractName,
                functionName: properties.functionName,
                blockHash: receiptCast.blockHash,
                description: description
              }
              console.log(receipt)
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  observer.next()
                  console.log(data)
                })
            })
            .on(('error'), (error) => {
              console.log(error)
              this.msgSvc.generalErrorMethod("error creatin new request")
            })
        })
    })
  }

  receiveContribution(requestNum: number) {
    let properties = SmartContract.crowdfundingFactory().receiveContributionFromProject(this.projectAddress, requestNum)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier$))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        console.log(abiFunction.encodedFunction)
        this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
            let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
            console.log(receipt)
            this.http.post(url, body)
              .pipe(takeUntil(this.notifier$))
              .subscribe((data) => {
                console.log(data)
              })
          })
          .on(('error'), (error) => {
            console.log(error)
          })
      })
  }

  // user functions they can interact with
  voteRequest(projectAddress: string, requestNum: number) {

    return new Observable(observer => {
      console.log(requestNum)
      let properties = SmartContract.crowdfundingFactory().voteRequestForProject(projectAddress, requestNum)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
            .on(('receipt'), (receipt) => {
              // on successful vote
              observer.next()

              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
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
  approveTokenSpendByContract(contractAddress: string, tokenAddress: string, walletAddress: string, amount: number) {
    return new Observable(observer => {

      let properties = SmartContract.TWLV().approve(contractAddress, amount)
      this.getEncodedFunctionVariableObservable(properties, tokenAddress)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, tokenAddress)
            .on(('receipt'), (receipt) => {
              // emiting true
              observer.next()

              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
              console.log(receipt)
              // sending blockhash to server to read event
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  console.log(data)
                })
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

      let properties = SmartContract.crowdfundingFactory().contributeToProject(projectAddress, amount)
      console.log(properties)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
            .on(('receipt'), (receipt) => {
              // on tx success
              observer.next()
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => { })
            })
            .on(('error'), (error) => {
              console.log(error)
              observer.error()
            })
        })
    })
  }

  refund(projectAddress: string) {
    return new Observable(observer => {

      let properties = SmartContract.crowdfundingFactory().getRefundFromProject(projectAddress)
      this.getEncodedFunctionVariableObservable(properties)
        .pipe(takeUntil(this.notifier$))
        .subscribe((response) => {
          let abiFunction = response as { encodedFunction: string }
          console.log(abiFunction.encodedFunction)
          this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
            .on(('receipt'), (receipt) => {
              // on successful refund
              observer.next()
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
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



  // add view functeon to see approved
  verifyTokenApproval() {

  }


  // view function Factory
  viewRequestForProject() {
    let properties = SmartContract.crowdfundingFactory().getRefundFromProject(this.projectAddress)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier$))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        this.callContract(abiFunction.encodedFunction, constants.FAUCET_CONTRACT_ADDRESS)
          .then((data) => {
            console.log(data)
          })
          .catch((error) => {
            console.log(error)
          })
      })
  }

  // FAUCET
  distributeFromFaucet() {
    let properties = SmartContract.twlvFaucet().distribute()
    console.log(properties)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier$))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        // this.web3.eth.handleRevert = true
        try {

          this.sendTransaction(abiFunction.encodedFunction, constants.FAUCET_CONTRACT_ADDRESS)
            // .then((receipt) => {
            //   console.log(receipt)
            // })
            // .catch(error => { console.log(error) })
            .on(('receipt'), (receipt) => {
              let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
              let url: string = new Url().add(constants.SERVER_URL).add("api/").add("read-event").getUrl()
              let body = { contractName: properties.contractName, blockNumber: receiptCast.blockNumber, functionName: properties.functionName, blockHash: receiptCast.blockHash }
              console.log(receipt)
              this.http.post(url, body)
                .pipe(takeUntil(this.notifier$))
                .subscribe((data) => {
                  console.log(data)
                })
            })
            .on(('error'), (error: any) => {
              this.msgSvc.generalErrorMethod("You probably have claimed from the faucet less than 25 hours ago")

              console.log(this.web3.eth.handleRevert)
              console.error(error.message)
              let receiptCast = error as { blockHash: string, logs: Object[], blockNumber: number }
              let a = error.message as { blockHash: string }
              console.log(a.blockHash)
              console.log(receiptCast.blockHash)

            })
        } catch (error) {
          console.log(error)
        }
      })
  }
  public getTokenBalance(tokenAddress: string, walletAddress: string) {
    let properties = SmartContract.TWLV().balanceOf(walletAddress)
    console.log(properties)
    return this.getTokenBalanceObservable(properties, tokenAddress)
  }

  private getTokenBalanceObservable(properties: ContractTxProperties, tokenAddress: string) {
    let requestBody: any[] = []
    console.log(properties)
    if (properties.parameters) {
      requestBody = properties.parameters
    }
    let url: string = new Url()
      .add(constants.SERVER_URL)
      .add("api/")
      .add("get-balance-function-encoded")
      .getUrl()
    console.log(requestBody)
    let params = new HttpParams()
      .set('tokenAddress', tokenAddress)
      .set('functionName', properties.functionName)
    return this.http.post(url, requestBody, { params })
  }

  private getEncodedFunctionVariableObservable(properties: ContractTxProperties, contractAddress?: string) {
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
    let address = this.storageSvc.getAddress()
    console.log("wallet address connected to", address)
    try {
      let a = this.web3.eth.sendTransaction({
        from: address as string,
        to: contractAddress as string,
        data: encodedFunction as string
      })
      return a
    } catch (error) {
      console.log(error)
      throw new Error("ok")
    }

  }

  private callContract(encodedFunction: string, contractAddress: string) {
    console.log(encodedFunction)
    console.log(contractAddress)
    return this.web3.eth.call({
      to: contractAddress,
      data: encodedFunction
    })
  }

  private loadContract() {
    let jsonUrl = "../../assets/tokenInterface/token.json"
    this.http.get(jsonUrl)
      .pipe(takeUntil(this.notifier$))
      .subscribe((jsonInterface) => {
        console.log(jsonInterface)
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true);
    this.notifier$.unsubscribe()
  }


}
