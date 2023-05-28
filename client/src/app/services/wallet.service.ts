import { Injectable, NgZone, Output } from '@angular/core';
import { Observable, Subject, Subscription, catchError, from, switchMap, takeUntil } from 'rxjs';

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
  subArr: Subscription[] = []

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
        chainId = this.web3.utils.toDecimal(chainId)
        if (chainId != 11155111) this.messageSvc.detailedWarnMethod('Chain is not Sepolia', 'Please change to Sepolia')
        if (chainId == 11155111) this.storageSvc.saveChain("sepolia")
        if (chainId == 1337) this.storageSvc.saveChain("ganache")
        if (chainId != 1337 && chainId != 11155111) this.storageSvc.saveChain("unknown")
        this.onChainIdChangeEvent.next(chainId)
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

  public switchChain() {
    return new Observable(observer => {
      window.ethereum.request({
        method: 'wallet_addEthereumChain',
        params: [
          {
            chainId: this.web3.utils.toHex(11155111),
            chainName: 'Sepolia',
            nativeCurrency: {
              name: 'ETH',
              symbol: 'ETH',
              decimals: 18
            },
            rpcUrls: ['https://eth-sepolia.public.blastapi.io'] /* ... */,
            blockExplorerUrls: ['https://sepolia.etherscan.io/']
          },
        ],
      }).then(() => {
        this.msgSvc.generalSuccessMethod("Successfully connected to Sepolia")
        observer.next()
      }).catch(() => {
        observer.error()
        this.msgSvc.generalErrorMethod("Failed to add chain")
      })

      // if error is other than 4902
      // } else {
      //   observer.error()
      //   this.msgSvc.generalErrorMethod("Failed to connect to chain")
      // }


      // window.ethereum.request({
      //   method: 'wallet_switchEthereumChain',
      //   params: [{ chainId: this.web3.utils.toHex(11155111) }],
      // }).then(() => { observer.next() })
      //   .catch((switchError: any) => {
      //     // This error code indicates that the chain has not been added to MetaMask.
      //     if (switchError.code === 4902) {
      //       window.ethereum.request({
      //         method: 'wallet_addEthereumChain',
      //         params: [
      //           {
      //             chainId: this.web3.utils.toHex(11155111),
      //             chainName: 'Sepolia',
      //             nativeCurrency: {
      //               name: 'ETH',
      //               symbol: 'ETH',
      //               decimals: 18
      //             },
      //             rpcUrls: ['https://eth-sepolia.public.blastapi.io'] /* ... */,
      //             blockExplorerUrls: ['https://sepolia.etherscan.io/']
      //           },
      //         ],
      //       }).then(() => { observer.next() })
      //         .catch(() => {
      //           observer.error()
      //           this.msgSvc.generalErrorMethod("Failed to add chain")
      //         })

      //       // if error is other than 4902
      //     } else {
      //       observer.error()
      //       this.msgSvc.generalErrorMethod("Failed to connect to chain")
      //     }
      //   })

    })
  }

  public connectWalletToWebsite() {
    return new Observable(observer => {
      let subscription$ = new Subscription

      subscription$ = this.connectWallet()
        .pipe()
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
      this.subArr.push(subscription$)

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

  // manually clean up service
  cleanUp() {
    this.subArr.forEach(sub => {
      console.log("sub getting unsub")
      sub.unsubscribe()
    });
    this.subArr = []
  }


}
