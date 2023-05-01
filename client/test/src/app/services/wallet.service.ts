import { Injectable, Output } from '@angular/core';
import { Subject, from, switchMap } from 'rxjs';

import Web3 from 'web3';
import { alertMessageService } from './alert.message.service';
import { StorageService } from './storage.service';

declare global {
  interface Window {
    ethereum: any;
  }
}

@Injectable({
  providedIn: 'root'
})
export class WalletService {

  walletAddressEvent = new Subject<string>
  walletAddress!: string
  isWalletConnectedEvent = new Subject<boolean>
  isWalletConnected!: boolean
  chainId!: number
  onChainIdChangeEvent = new Subject<number>

  web3: Web3 = new Web3(window.ethereum);

  constructor(private messageSvc: alertMessageService, private storageSvc: StorageService) {
    if (window.ethereum) {
      window.ethereum.on('accountsChanged', async (accounts: string | any[]) => {
        if (accounts.length >= 1) {
          this.walletAddress = accounts[0]
          this.walletAddressEvent.next(this.walletAddress)
          this.chainId = await this.getChain()
          this.onChainIdChangeEvent.next(this.chainId)
        } else {
          // disconnected
          this.walletAddress = ""
          this.walletAddressEvent.next(this.walletAddress)
          this.storageSvc.clean()
        }
      })
      window.ethereum.on('chainChanged', (chainId: any) => {
        this.onChainIdChangeEvent.next(chainId)
      })
      this.getAccounts().then((accounts) => {
        if (accounts.length != 0) {
          this.walletAddressEvent.next(accounts[0])
          this.getChain().then((chainId) => {
            this.onChainIdChangeEvent.next(chainId)
          })
        }
      })
    }
  }

  private async getAccounts() {
    return await this.web3.eth.getAccounts()
  }

  private async getChain() {
    return await this.web3.eth.getChainId()
  }

  public async connectWallet() {
    if (window.ethereum) {
      this.messageSvc.connectWalletMsg()

      return await window.ethereum.request({ method: 'eth_requestAccounts' })
    }
    this.messageSvc.requestToInstallMetamask()
  }

  public shortenAddress(address: string) {
    let indexlength = address.length;
    return address.substring(0, 6) + "..." + address.substring(indexlength - 4, indexlength)
  }
}
