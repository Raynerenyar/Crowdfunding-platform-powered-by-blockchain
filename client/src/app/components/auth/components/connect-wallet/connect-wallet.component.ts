import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject, Subscription, takeUntil } from 'rxjs';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { AuthService } from 'src/app/services/auth.service';
import { WalletService } from 'src/app/services/wallet.service';
import { SessionStorageService } from '../../../../services/session.storage.service';

@Component({
  selector: 'app-connect-wallet',
  templateUrl: './connect-wallet.component.html',
  styleUrls: ['./connect-wallet.component.css']
})
export class ConnectWalletComponent implements OnInit, OnDestroy {

  walletAddress!: string
  shortenedAddress!: string
  walletAddressSub$!: Subscription
  notifier$ = new Subject<boolean>()

  constructor(private walletSvc: WalletService, private storageSvc: SessionStorageService, private cdr: ChangeDetectorRef, private msgSvc: PrimeMessageService) { }

  ngOnInit(): void {
    this.walletAddressSub$ = this.walletSvc.walletAddressEvent
      .subscribe((address: string) => {
        this.walletAddress = address
        if (address == "") {
          this.shortenedAddress = ""
          this.cdr.detectChanges()
        } else {
          this.shortenedAddress = this.walletSvc.shortenAddress(address)
          this.cdr.detectChanges()
        }
      })
  }

  // duplidate method in overlay panel component
  connectToWallet() {
    if (!this.walletAddress) {
      this.walletSvc.connectWalletToWebsite()
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (addressArr) => {
            this.msgSvc.generalSuccessMethod("You have successfully connected")
            let address = addressArr as string[]
            this.msgSvc.generalSuccessMethod("You have successfully connected")
            this.storageSvc.saveAddress(address[0])

          },
          error: (err) => { },
        })
    }
  }

  ngOnDestroy(): void {
    if (this.walletAddressSub$) this.walletAddressSub$.unsubscribe()
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
    this.walletSvc.cleanUp()
  }

}
