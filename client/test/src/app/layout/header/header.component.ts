import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AlertMessageService } from '../../services/alert.message.service';
import { Observable, Subscription, timer } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { StorageService } from 'src/app/services/storage.service';
import { OverlayPanel } from 'primeng/overlaypanel';
import { ConfirmationService } from 'primeng/api';
import { Router } from '@angular/router';

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

  constructor(private authSvc: AuthService, private storageService: StorageService, private msgSvc: AlertMessageService, private confirmSvc: ConfirmationService, private router: Router, private cdr: ChangeDetectorRef) { }


  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
    } else this.isLoggedIn = false;
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
        this.authSvc.signOut()
        this.storageService.clean()

        this.logoutSub$ = this.authSvc.logout().subscribe({
          next: res => {
            console.log(res)
            this.msgSvc.signedOut();
            this.storageService.clean();
            this.isLoggedIn = false
          },
          error: err => {
            console.log(err);
            this.msgSvc.generalErrorMethod(err);
          }
        });

        this.timerSub$ = timer(4000).subscribe(t => {
          this.router.navigate([''])
          // window.location.reload()
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

  ngOnDestroy(): void {
    if (this.logoutSub$) this.logoutSub$.unsubscribe()
    if (this.timerSub$) this.timerSub$.unsubscribe()
    if (this.signInSub$) this.signInSub$.unsubscribe()
  }
}
