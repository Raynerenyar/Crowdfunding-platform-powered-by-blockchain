import { Injectable, OnDestroy } from '@angular/core';
import { SmartContract } from '../smartContractRelated/contracts';
import Web3 from 'web3';
import { HttpClient, HttpParams } from '@angular/common/http';
import { constants } from 'src/environments/environment';
import { ContractTxProperties } from '../model/model';
import { Url } from '../util/url.util';
import { AuthService } from './auth.service';
import { Subject, takeUntil } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BlockchainService implements OnDestroy {

  projectAddress!: string

  web3: Web3 = new Web3(window.ethereum);

  notifier = new Subject();

  constructor(private http: HttpClient, private authSvc: AuthService) { }

  // project owner functions they can interact with
  createProject(goal: number, deadline: number, tokenAddress: string, description: string) {
    let properties = SmartContract.crowdfundingFactory().createNewProject(goal, deadline, tokenAddress, description)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        console.log(abiFunction.encodedFunction)
        this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let data = receiptCast.logs[2] as { data: string }
            console.log(receiptCast)
            console.log(receiptCast.blockHash)
            console.log(receiptCast.logs[2])

            let url: string = new Url().add(constants.SERVER_URL).add("read-event").getUrl()
            let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }

            this.http.post(url, body)
              .pipe(takeUntil(this.notifier))
              .subscribe((data) => {
                console.log(data)
              })
          })
          .on(('error'), (error) => {
            console.log(error)
          })
      })
  }

  createRequest(title: string, description: string, recipient: string, amount: number) {
    let properties = SmartContract.crowdfundingFactory().createRequestForProject(this.projectAddress, title, recipient, amount)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        console.log(abiFunction.encodedFunction)
        this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let url: string = new Url().add(constants.SERVER_URL).add("read-event").getUrl()
            let body = {
              contractName: properties.contractName,
              functionName: properties.functionName,
              blockHash: receiptCast.blockHash,
              description: description
            }
            console.log(receipt)
            this.http.post(url, body)
              .pipe(takeUntil(this.notifier))
              .subscribe((data) => {
                console.log(data)
              })
          })
          .on(('error'), (error) => {
            console.log(error)
          })
      })
  }

  receiveContribution(requestNum: number) {
    let properties = SmartContract.crowdfundingFactory().receiveContributionFromProject(this.projectAddress, requestNum)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        console.log(abiFunction.encodedFunction)
        this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let url: string = new Url().add(constants.SERVER_URL).add("read-event").getUrl()
            let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
            console.log(receipt)
            this.http.post(url, body)
              .pipe(takeUntil(this.notifier))
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
  voteRequest(requestNum: number) {
    let properties = SmartContract.crowdfundingFactory().voteRequestForProject(this.projectAddress, requestNum)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        console.log(abiFunction.encodedFunction)
        this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let url: string = new Url().add(constants.SERVER_URL).add("read-event").getUrl()
            let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
            console.log(receipt)
            this.http.post(url, body)
              .pipe(takeUntil(this.notifier))
              .subscribe((data) => {
                console.log(data)
              })
          })
          .on(('error'), (error) => {
            console.log(error)
          })
      })
  }

  contribute(amount: number) {
    let properties = SmartContract.crowdfundingFactory().contributeToProject(this.projectAddress, amount)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        console.log(abiFunction.encodedFunction)
        this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let url: string = new Url().add(constants.SERVER_URL).add("read-event").getUrl()
            let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
            console.log(receipt)
            this.http.post(url, body)
              .pipe(takeUntil(this.notifier))
              .subscribe((data) => {
                console.log(data)
              })
          })
          .on(('error'), (error) => {
            console.log(error)
          })
      })
  }

  refund() {
    let properties = SmartContract.crowdfundingFactory().getRefundFromProject(this.projectAddress)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        console.log(abiFunction.encodedFunction)
        this.sendTransaction(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let url: string = new Url().add(constants.SERVER_URL).add("read-event").getUrl()
            let body = { contractName: properties.contractName, functionName: properties.functionName, blockHash: receiptCast.blockHash }
            console.log(receipt)
            this.http.post(url, body)
              .pipe(takeUntil(this.notifier))
              .subscribe((data) => {
                console.log(data)
              })
          })
          .on(('error'), (error) => {
            console.log(error)
          })
      })
  }

  // view function Factory
  viewRequestForProject() {
    let properties = SmartContract.crowdfundingFactory().getRefundFromProject(this.projectAddress)
    this.getEncodedFunctionVariableObservable(properties)
      .pipe(takeUntil(this.notifier))
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
      .pipe(takeUntil(this.notifier))
      .subscribe((response) => {
        let abiFunction = response as { encodedFunction: string }
        this.sendTransaction(abiFunction.encodedFunction, constants.FAUCET_CONTRACT_ADDRESS)
          .on(('receipt'), (receipt) => {
            let receiptCast = receipt as { blockHash: string, logs: Object[], blockNumber: number }
            let url: string = new Url().add(constants.SERVER_URL).add("read-event").getUrl()
            let body = { contractName: properties.contractName, blockNumber: receiptCast.blockNumber, functionName: properties.functionName, blockHash: receiptCast.blockHash }
            console.log(receipt)
            this.http.post(url, body)
              .pipe(takeUntil(this.notifier))
              .subscribe((data) => {
                console.log(data)
              })
          })
          .on(('error'), (error) => {
            console.log(error)
          })
      })
  }

  private getEncodedFunctionVariableObservable(properties: ContractTxProperties) {
    let requestBody: any[] = []
    if (properties.parameters) {
      requestBody = properties.parameters
    }
    let url: string = new Url()
      .add(constants.SERVER_URL)
      .add("get-function-encoded/")
      .getUrl()
    console.log(url)
    let params = new HttpParams()
      .set('contractName', properties.contractName)
      .set('functionName', properties.functionName)
    console.log(requestBody)
    return this.http.post(url, requestBody, { params })
  }

  private sendTransaction(encodedFunction: string, contractAddress: string) {
    console.log("wallet address connected to", this.authSvc.walletAddress)
    return this.web3.eth.sendTransaction({
      from: this.authSvc.walletAddress,
      to: contractAddress,
      data: encodedFunction as string
    })
  }

  private callContract(encodedFunction: string, contractAddress: string) {
    console.log(encodedFunction)
    console.log(contractAddress)
    return this.web3.eth.call({
      to: contractAddress,
      data: encodedFunction
    })
  }

  ngOnDestroy(): void {
    this.notifier.next("");
    this.notifier.complete()
  }
}
