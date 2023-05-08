import { Injectable, NgZone, Output } from '@angular/core';
import { Subject, from, switchMap } from 'rxjs';

import Web3 from 'web3';
import { PrimeMessageService } from './prime.message.service';
import { SessionStorageService } from './session.storage.service';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

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

  constructor(private messageSvc: PrimeMessageService, private storageSvc: SessionStorageService, private router: Router, private ngZone: NgZone, private authSvc: AuthService) {
    if (window.ethereum) {
      window.ethereum.on('accountsChanged', async (accounts: string | any[]) => {
        if (accounts.length >= 1) {
          this.storageSvc.clean();
          this.walletAddress = accounts[0]
          this.walletAddressEvent.next(this.walletAddress)
          this.chainId = await this.getChain()
          this.onChainIdChangeEvent.next(this.chainId)
          this.storageSvc.saveAddress(this.walletAddress)
          console.log("accounts changed")
          if (this.storageSvc.isLoggedIn())
            this.authSvc.logout()
          window.location.reload()
          this.messageSvc.generalWarnMethod('Logged out', 'You have been logged out')
        } else {
          // disconnected
          if (this.storageSvc.isLoggedIn())
            this.authSvc.logout()

          this.storageSvc.clean();
          this.walletAddress = ""
          this.walletAddressEvent.next(this.walletAddress)
          this.storageSvc.clearAddress()
          this.ngZone.run(() => {
            this.router.navigate([''])
          });
          this.messageSvc.generalWarnMethod('Logged out', 'You have been logged out')
        }
      })
      window.ethereum.on('chainChanged', (chainId: any) => {
        this.onChainIdChangeEvent.next(chainId)
        if (chainId != 11155111) this.messageSvc.generalWarnMethod('Chain is not Sepolia', 'Please change to Sepolia')
        // this.messageSvc.generalWarnMethod()
        /*       if (chainId == 11155111) this.chainName = "Sepolia"
      if (chainId == 1337) this.chainName = "Ganache" */
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
