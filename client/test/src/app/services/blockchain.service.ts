import { Injectable } from '@angular/core';
import { SmartContract } from '../smartContractRelated/contracts';
import Web3 from 'web3';
import { HttpClient, HttpParams } from '@angular/common/http';
import { constants } from 'src/environments/environment';
import { ContractTxProperties } from '../model/model';
import { Url } from '../util/util';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class BlockchainService {

  projectAddress!: string

  web3: Web3 = new Web3(window.ethereum);
  constructor(private http: HttpClient, private authSvc: AuthService) { }

  // project owner functions they can interact with
  createProject(goal: number, timeAhead: number, tokenAddress: string, description: string) {
    let properties = SmartContract.crowdfundingFactory().createNewProject(goal, timeAhead, tokenAddress, description)
    this.getEncodedFunctionVariableObservable(properties).subscribe((response) => {
      let abiFunction = response as { encodedFunction: string }
      console.log(abiFunction.encodedFunction)
      this.interactWithContract(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS).then((receipt) => {
        let receiptCast = receipt as { blockHash: string, logs: Object[] }
        let data = receiptCast.logs[2] as { data: string }
        console.log(receiptCast.blockHash)
        console.log(receiptCast.logs[2])
        // console.log(this.web3.eth.abi.decodeParameters(data))
      })
    })
  }

  createRequest(description: string, recipient: string, amount: number) {
    let properties = SmartContract.crowdfundingFactory().createRequestForProject(this.projectAddress, description, recipient, amount)
    this.getEncodedFunctionVariableObservable(properties).subscribe((response) => {
      let abiFunction = response as { encodedFunction: string }
      this.interactWithContract(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS).then((receipt) => {
        console.log(receipt)
      })
      console.log(abiFunction.encodedFunction)
    })
  }

  receiveContribution(requestNum: number) {
    let properties = SmartContract.crowdfundingFactory().receiveContributionFromProject(this.projectAddress, requestNum)
    this.getEncodedFunctionVariableObservable(properties).subscribe((response) => {
      let abiFunction = response as { encodedFunction: string }
      this.interactWithContract(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS).then((receipt) => {
        console.log(receipt)
      })
      console.log(abiFunction.encodedFunction)
    })
  }

  // user functions they can interact with
  voteRequest(requestNum: number) {
    let properties = SmartContract.crowdfundingFactory().voteRequestForProject(this.projectAddress, requestNum)
    this.getEncodedFunctionVariableObservable(properties).subscribe((response) => {
      let abiFunction = response as { encodedFunction: string }
      this.interactWithContract(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS).then((receipt) => {
        console.log(receipt)
      })
      console.log(abiFunction.encodedFunction)
    })
  }

  contribute(amount: number) {
    let properties = SmartContract.crowdfundingFactory().contributeToProject(this.projectAddress, amount)
    this.getEncodedFunctionVariableObservable(properties).subscribe((response) => {
      let abiFunction = response as { encodedFunction: string }
      this.interactWithContract(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS).then((receipt) => {
        console.log(receipt)
      })
      console.log(abiFunction.encodedFunction)
    })
  }

  refund() {
    let properties = SmartContract.crowdfundingFactory().getRefundFromProject(this.projectAddress)
    this.getEncodedFunctionVariableObservable(properties).subscribe((response) => {
      let abiFunction = response as { encodedFunction: string }
      this.interactWithContract(abiFunction.encodedFunction, constants.FACTORY_CONTRACT_ADDRESS).then((receipt) => {
        console.log(receipt)
      })
      console.log(abiFunction.encodedFunction)
    })
  }



  private getEncodedFunctionVariableObservable(properties: ContractTxProperties) {
    let requestBody: any[] = []
    if (properties.parameters!.length != 0) {
      requestBody = properties.parameters!
    }
    let url: string = new Url()
      .add(constants.SERVER_URL)
      .add("get-function-encoded/")
      .getUrl()
    console.log(url)
    let params = new HttpParams()
      .set('contractName', properties.contractName)
      .set('functionName', properties.functionName)
    return this.http.post(url, requestBody, { params })
  }

  interactWithContract(encodedFunction: string, contractAddress: string) {
    if (encodedFunction.length > 10) {
      return this.sendTransaction(encodedFunction, contractAddress)
    } else {
      return this.callContract(encodedFunction)
    }
  }


  private sendTransaction(encodedFunction: string, contractAddress: string) {
    console.log(this.authSvc.walletAddress)
    return this.web3.eth.sendTransaction({
      from: this.authSvc.walletAddress,
      to: contractAddress,
      data: encodedFunction as string
    })
    // .on(('error'), (error) => {
    //   console.log('error', error)
    // }).on(('confirmation'), (c, r, blockHash) => {
    //   console.log(blockHash) // need to send back to server to listen to events
    // })
  }

  private callContract(encodedFunction: string) {
    return this.web3.eth.call({
      to: this.authSvc.walletAddress,
      data: encodedFunction
    })
    // this.web3.eth.call({
    //   to: this.authSvc.walletAddress,
    //   data: encodedFunction
    // }, (error, result) => {
    //   if (error) {
    //     console.log(error)
    //   } else {

    //     console.log(this.web3.utils.hexToNumberString(result))
    //   }
    // })
  }
}
