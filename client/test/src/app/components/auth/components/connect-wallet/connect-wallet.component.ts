import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
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
  shortenedAddress!: string;
  walletAddressSub$!: Subscription;

  constructor(private alertMsgSvc: PrimeMessageService, private walletSvc: WalletService, private storageSvc: SessionStorageService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.walletAddressSub$ = this.walletSvc.walletAddressEvent.subscribe((address: string) => {
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
  ngOnDestroy(): void {
    if (this.walletAddressSub$) this.walletAddressSub$.unsubscribe()
  }

  connectToWallet() {
    if (!this.walletAddress) {
      this.walletSvc.connectWallet().then()
    }
  }
}
