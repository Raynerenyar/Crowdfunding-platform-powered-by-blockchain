import { Injectable, NgZone, Output } from '@angular/core';
import { Observable, Subject, catchError, from, switchMap, takeUntil } from 'rxjs';

import Web3 from 'web3';
import { PrimeMessageService } from './prime.message.service';
import { SessionStorageService } from './session.storage.service';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import detectEthereumProvider from '@metamask/detect-provider';

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
  notifier$ = new Subject<boolean>()

  web3: Web3 = new Web3(window.ethereum);

  constructor(
    private messageSvc: PrimeMessageService,
    private storageSvc: SessionStorageService,
    private router: Router,
    private ngZone: NgZone,
    private authSvc: AuthService,
    private msgSvc: PrimeMessageService,
  ) {

    if (window.ethereum) {

      // on wallet change 
      window.ethereum.on('accountsChanged', async (accounts: string | any[]) => {
        if (accounts.length >= 1) {
          console.log("in accounts change")
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
          this.messageSvc.detailedWarnMethod('Logged out', 'You have been logged out')
        } else {

          // on wallet disconnects
          if (this.storageSvc.isLoggedIn())
            this.authSvc.logout()

          this.storageSvc.clean();
          this.walletAddress = ""
          this.walletAddressEvent.next(this.walletAddress)
          this.storageSvc.clearAddress()
          this.ngZone.run(() => {
            this.router.navigate(['']).then(() => window.location.reload())
          });
          this.messageSvc.detailedWarnMethod('Logged out', 'You have been logged out')
        }
      })

      // on chain changed
      window.ethereum.on('chainChanged', (chainId: any) => {
        this.onChainIdChangeEvent.next(chainId)
        if (chainId != 11155111) this.messageSvc.detailedWarnMethod('Chain is not Sepolia', 'Please change to Sepolia')
        if (chainId == 11155111) this.storageSvc.saveChain("sepolia")
        if (chainId == 1337) this.storageSvc.saveChain("ganache")
        if (chainId != 1337 && chainId != 11155111) this.storageSvc.saveChain("unknown")
      })
      this.getAccounts().then((accounts) => {
        if (accounts.length != 0) {
          this.walletAddressEvent.next(accounts[0])
          this.getChain().then((chainId) => {
            if (chainId == 11155111) this.storageSvc.saveChain("sepolia")
            if (chainId == 1337) this.storageSvc.saveChain("ganache")
            if (chainId != 1337 && chainId != 11155111) this.storageSvc.saveChain("unknown")
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

  public isOnRightChain(): boolean {
    if (['sepolia', 'ganache'].includes(this.storageSvc.getChain())) {
      return true
    }
    return false
  }

  public connectWalletToWebsite() {
    return new Observable(observer => {

      this.connectWallet()
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (value) => {
            observer.next(value)
          },
          error: (err: Error) => {
            if (err.message.includes("Please install MetaMask"))
              this.msgSvc.detailedInfoMethod("Install MetaMask", "Please insall MetaMask")
            if (err.message.includes("Already processing eth_requestAccounts") || err.message.includes("wallet_requestPermissions"))
              this.msgSvc.detailedInfoMethod("Login To MetaMask!", "Please open your MetaMask extension and login.")
          },
        })
    })
  }

  private connectWallet() {
    return from(detectEthereumProvider()).pipe(
      switchMap(async (provider) => {
        if (!provider) { throw new Error('Please install MetaMask'); }
        return await window.ethereum.request({ method: 'eth_requestAccounts' });
      }),
      catchError((err) => {
        // let errorMessage = err as Error
        // console.log(errorMessage.name)
        // console.log(errorMessage.message)
        throw err
        // throw new Error(errorMessage.message);
      })
    )
  }

  public shortenAddress(address: string) {
    let indexlength = address.length;
    return address.substring(0, 6) + "..." + address.substring(indexlength - 4, indexlength)
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
