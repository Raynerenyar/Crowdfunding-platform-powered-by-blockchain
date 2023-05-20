import { AfterContentInit, ChangeDetectorRef, Component, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { PrimeMessageService } from '../../services/prime.message.service';
import { Observable, Subscription, timer } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { OverlayPanel } from 'primeng/overlaypanel';
import { ConfirmationService } from 'primeng/api';
import { Router } from '@angular/router';
import { PrimeNGConfig, OverlayOptions } from 'primeng/api';
import { ConfirmPopup } from 'primeng/confirmpopup';
import { DomHandler } from 'primeng/dom';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy, AfterContentInit {
  // isVisible!: boolean;
  isLoggedIn = false;

  signInSub$!: Subscription
  logoutSub$!: Subscription;
  timerSub$!: Subscription;

  @ViewChild('op')
  overlayPanel!: OverlayPanel
  @ViewChild('cpop')
  confirmPopup!: ConfirmPopup

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
  ngAfterContentInit(): void {

  }

  signOut(event: Event) {
    // const containerOffset = DomHandler.getOffset(this.confirmPopup.container)
    // const targetOffset = DomHandler.getOffset(this.confirmPopup.confirmation.target)
    // console.log(containerOffset)
    // console.log(targetOffset)
    // let arrowLeft = 0
    // if (containerOffset.left < targetOffset.left) {
    //   arrowLeft = targetOffset.left - containerOffset.left;
    // }
    // this.confirmPopup.container.style.setProperty('--overlayArrowLeft', `${1000}px`);


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
            this.msgSvc.generalSuccessMethod("You have logged out.")
            this.overlayPanel.hide()
            this.router.navigate(['explore']).then(() => window.location.reload())
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

  @HostListener('window:scroll', ['$event'])
  onWindowScroll(event: any) {
    if (this.overlayPanel.onShow) this.overlayPanel.hide()
    if (this.confirmPopup) this.confirmPopup.hide()
  }

  ngOnDestroy(): void {
    if (this.logoutSub$) this.logoutSub$.unsubscribe()
    if (this.timerSub$) this.timerSub$.unsubscribe()
    if (this.signInSub$) this.signInSub$.unsubscribe()
  }
}
