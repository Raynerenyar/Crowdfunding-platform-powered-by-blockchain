import { AfterContentInit, ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { PrimeMessageService } from '../../services/prime.message.service';
import { Observable, Subject, Subscription, takeUntil, timer } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { OverlayPanel } from 'primeng/overlaypanel';
import { ConfirmationService } from 'primeng/api';
import { Router } from '@angular/router';
import { PrimeNGConfig, OverlayOptions } from 'primeng/api';
import { ConfirmPopup } from 'primeng/confirmpopup';
import { DomHandler } from 'primeng/dom';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  // isVisible!: boolean;
  isLoggedIn = false;

  signInSub$!: Subscription
  logoutSub$!: Subscription;
  timerSub$!: Subscription;

  @ViewChild('op')
  overlayPanel!: OverlayPanel
  @ViewChild('cpop')
  confirmPopup!: ConfirmPopup
  onRightChain = false
  chainSub$!: Subscription
  notifier$ = new Subject<boolean>()

  constructor(
    private authSvc: AuthService,
    private storageService: SessionStorageService,
    private msgSvc: PrimeMessageService,
    private confirmSvc: ConfirmationService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private primengConfig: PrimeNGConfig,
    private walletSvc: WalletService
  ) { }


  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
    } else this.isLoggedIn = false;
    let o: OverlayOptions = this.primengConfig.overlayOptions
    o = {
      appendTo: 'body'
    }
    this.onRightChain = this.walletSvc.isOnRightChain()
    this.chainSub$ = this.walletSvc.onChainIdChangeEvent.subscribe(() => {
      this.onRightChain = this.walletSvc.isOnRightChain()
      this.cdr.detectChanges()
    })

  }

  signOut(event: Event) {

    // show popup to get confirmation on logging out
    this.confirmSvc.confirm({
      target: event.target ?? undefined,
      message: "Confirm Logout.",
      icon: 'pi pi-exclamation-triangle',

      accept: () => {
        // this.authSvc.signOut()
        this.authSvc.logout()
        this.storageService.clean()

        this.logoutSub$ = this.authSvc.logout().subscribe({
          next: res => {
            console.log(res)
            this.msgSvc.signedOut()
            this.storageService.clean()
            this.isLoggedIn = false
            this.overlayPanel.hide()
            this.msgSvc.generalSuccessMethod("You have logged out")
            this.reload(4000)
              .pipe(takeUntil(this.notifier$))
              .subscribe({
                next: () => {
                  this.router.navigate(['explore']).then(() => window.location.reload())
                }
              })
          },
          error: err => {
            console.log(err);
            this.msgSvc.generalErrorMethod(err);
          }
        });
      },

      reject: () => {
        this.msgSvc.generalErrorMethod("You have rejected logging out.")
      }
    })

  }

  hideOverlay() {
    console.log("hiding overlay")
    this.overlayPanel.hide()
  }

  private reload(miliseconds: number) {
    return timer(miliseconds)
  }


  @HostListener('window:scroll', ['$event'])
  onWindowScroll(event: any) {
    if (this.overlayPanel.onShow) this.overlayPanel.hide()
    if (this.confirmPopup) this.confirmPopup.hide()
  }

  connectToSepolia() {
    this.walletSvc.switchChain()
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: () => {

        },
        error: (err) => {

        },
      })
  }

  ngOnDestroy(): void {
    if (this.logoutSub$) this.logoutSub$.unsubscribe()
    if (this.timerSub$) this.timerSub$.unsubscribe()
    if (this.signInSub$) this.signInSub$.unsubscribe()
    if (this.chainSub$) this.chainSub$.unsubscribe()
    if (this.timerSub$) this.timerSub$.unsubscribe()
  }
}
