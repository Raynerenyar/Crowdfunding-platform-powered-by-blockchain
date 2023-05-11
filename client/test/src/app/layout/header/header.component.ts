import { ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { PrimeMessageService } from '../../services/prime.message.service';
import { Observable, Subscription, timer } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { OverlayPanel } from 'primeng/overlaypanel';
import { ConfirmationService } from 'primeng/api';
import { Router } from '@angular/router';
import { PrimeNGConfig, OverlayOptions } from 'primeng/api';
import { ConfirmPopup } from 'primeng/confirmpopup';

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
  confirmPoput!: ConfirmPopup

  constructor(private authSvc: AuthService, private storageService: SessionStorageService, private msgSvc: PrimeMessageService, private confirmSvc: ConfirmationService, private router: Router, private cdr: ChangeDetectorRef, private primengConfig: PrimeNGConfig) { }


  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
    } else this.isLoggedIn = false;
    let o: OverlayOptions = this.primengConfig.overlayOptions
    o = {
      appendTo: 'body'
    }
  }

  signIn() {

    this.signInSub$ = this.authSvc.signInWithWeb3().subscribe(
      // can check for error and show appropriate message on view
      (data) => {
        console.log(data)

        // this.authSvc.
      }
    )
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
            this.reloadPage()
            this.msgSvc.generalSuccessMethod("You have logged out.")
          },
          error: err => {
            console.log(err);
            this.msgSvc.generalErrorMethod(err);
          }
        });

        this.timerSub$ = timer(4000).subscribe(t => {
          this.router.navigate([''])
        })
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

  reloadPage(): void {
    this.timerSub$ = timer(4000).subscribe(t => {
      window.location.reload();
    })
  }

  @HostListener('window:scroll', ['$event'])
  onWindowScroll(event: any) {
    if (this.overlayPanel.onShow) this.overlayPanel.hide()
    if (this.confirmPoput) this.confirmPoput.hide()
  }

  ngOnDestroy(): void {
    if (this.logoutSub$) this.logoutSub$.unsubscribe()
    if (this.timerSub$) this.timerSub$.unsubscribe()
    if (this.signInSub$) this.signInSub$.unsubscribe()
  }
}
