import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { UserService } from '../../../../services/user.service';
import { SessionStorageService } from '../../../../services/session.storage.service';
import { Subscription } from 'rxjs';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-board-user',
  templateUrl: './board-user.component.html',
  styleUrls: ['./board-user.component.css']
})
export class BoardUserComponent implements OnInit, OnDestroy {

  // isLoggedIn = false
  content?: string;
  walletAddress!: string
  chainId!: number
  chainName!: string
  isLoggedIn = false;
  private roles: string[] = []


  onWalletAddressSub$!: Subscription
  onChainIdChangeSub$!: Subscription
  onGettingUserBoard$!: Subscription

  constructor(
    private userService: UserService,
    private walletSvc: WalletService,
    private storageSvc: SessionStorageService,
    private cdr: ChangeDetectorRef
  ) { }
  ngOnInit(): void {
    // check if logged in to project creator account
    if (this.storageSvc.isLoggedIn()) {
      const user = this.storageSvc.getUser();
      this.roles = user.roles;
      this.isLoggedIn = this.roles.includes('ROLE_USER')

      this.onGettingUserBoard$ = this.userService.getUserBoard().subscribe({
        next: data => {
          this.content = data;
        },
        error: err => {
          // console.log(err)
          if (err.error) {
            this.content = JSON.parse(err.error).message;
          } else {
            this.content = "Error with status: " + err.status;
          }
        }
      });
    } else this.isLoggedIn = false;

    this.onWalletAddressSub$ = this.walletSvc.walletAddressEvent.subscribe((address) => {
      this.walletAddress = address
      this.cdr.detectChanges()
    })
    this.onChainIdChangeSub$ = this.walletSvc.onChainIdChangeEvent.subscribe((chainId) => {
      this.chainId = chainId
      if (chainId == 11155111) this.chainName = "Sepolia"
      if (chainId == 1337) this.chainName = "Ganache"
      this.cdr.detectChanges()
    })
  }

  ngOnDestroy(): void {
    if (this.onGettingUserBoard$) this.onGettingUserBoard$.unsubscribe()
    if (this.onChainIdChangeSub$) this.onChainIdChangeSub$.unsubscribe()
    if (this.onWalletAddressSub$) this.onWalletAddressSub$.unsubscribe()
  }
}