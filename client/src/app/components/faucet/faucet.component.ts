import { Component, OnDestroy } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';

@Component({
  selector: 'app-faucet',
  templateUrl: './faucet.component.html',
  styleUrls: ['./faucet.component.css']
})
export class FaucetComponent implements OnDestroy {

  isLoading = false
  notifier$ = new Subject<boolean>()

  constructor(private bcSvc: BlockchainService, private storageSvc: SessionStorageService, private msgSvc: PrimeMessageService) { }

  distribute() {
    if (this.storageSvc.getAddress()) {
      this.isLoading = true
      this.bcSvc.distributeFromFaucet()
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (value) => {
            this.isLoading = false
            this.msgSvc.generalSuccessMethod("You have received some DEV")
          },
          error: (err) => {
            this.isLoading = false
            this.msgSvc.generalErrorMethod("You probaby have recently used the faucet")
          }
        })
      return
    }
    this.msgSvc.generalErrorMethod("Wallet Not Connected")
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
