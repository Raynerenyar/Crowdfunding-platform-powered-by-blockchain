import { Component, Input, OnInit, Output } from '@angular/core';
import { OverlayPanel } from 'primeng/overlaypanel';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-overlayPanel',
  templateUrl: './overlay.panel.component.html',
  styleUrls: ['./overlay.panel.component.css']
})
export class OverlayPanelComponent implements OnInit {

  isLoggedIn = false;

  @Output()
  onLoginEvent = new Subject<string>()
  walletAddress!: string
  roles: string[] = [];
  notifier$ = new Subject<boolean>()
  isWalletConnected = false

  constructor(private storageSvc: SessionStorageService, private msgSvc: PrimeMessageService, private walletSvc: WalletService) { }

  ngOnInit(): void {
    if (this.storageSvc.isLoggedIn()) {
      this.isLoggedIn = true
    } else this.isLoggedIn = false

    this.walletAddress = this.storageSvc.getAddress()
    if (this.walletAddress.length == 0) {
      this.isWalletConnected = false
    } else this.isWalletConnected = true
  }

  // duplicate method in connect wallet component
  connectWallet() {
    this.walletSvc.connectWalletToWebsite()
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (addressArr) => {
          let address = addressArr as string[]
          this.isWalletConnected = true
          this.msgSvc.generalSuccessMethod("You have successfully connected")
          this.storageSvc.saveAddress(address[0])
        },
        error: (error) => { }
      })
  }

  emitLoginEvent() {
    this.onLoginEvent.next("login")
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
