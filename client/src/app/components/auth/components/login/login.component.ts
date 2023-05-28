/* Login Component also uses AuthService to work with
 Observable object. Besides that, it calls StorageService 
 methods to check loggedIn status and save User info to Session Storage. */
import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { SessionStorageService } from '../../../../services/session.storage.service';
import { WalletService } from 'src/app/services/wallet.service';
import { Subject, Subscription, takeUntil, timer } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../../services/auth.service'
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { OverlayPanel } from 'primeng/overlaypanel';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  form: any = {
    username: null,
    password: null
  };
  isLoggedIn = false
  isLoginFailed = false
  isLoggingIn = false
  isLoading = false
  errorMessage = ''
  roles: string[] = []

  loginForm!: FormGroup
  walletAddress!: string
  chainId!: number
  chainName!: string
  shortenedAddress!: string
  minLength = 10
  maxLength = 20
  isWalletSigned = false
  isSignedMsgFailed = false

  onChainIdChangeSub$!: Subscription
  onWalletAddressSub$!: Subscription

  timerSub$!: Subscription
  notifier$ = new Subject<boolean>()

  @Output()
  onLoginEvent = new Subject<string>()

  constructor(
    private storageService: SessionStorageService,
    private walletSvc: WalletService,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder,
    private authSvc: AuthService,
    private msgSvc: PrimeMessageService,
    private router: Router,
    private storageSvc: SessionStorageService
  ) { }

  ngOnInit(): void {
    if (this.storageSvc.isLoggedIn()) {
      this.roles = this.storageSvc.getUser().roles
      const user = this.storageSvc.getUser()
      this.isLoggedIn = this.roles.includes('ROLE_USER')
    }
    this.onWalletAddressSub$ = this.walletSvc.walletAddressEvent.subscribe((address) => {
      this.walletAddress = address
      this.shortenedAddress = this.walletSvc.shortenAddress(address)
      this.cdr.detectChanges()
    })
    this.onChainIdChangeSub$ = this.walletSvc.onChainIdChangeEvent.subscribe((chainId) => {
      this.chainId = chainId
      this.chainName = "Unknown"
      if (chainId == 11155111) this.chainName = "Sepolia"
      if (chainId == 1337) this.chainName = "Ganache"
      this.cdr.detectChanges()
    })
    this.loginForm = this.fb.group({
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(this.minLength), Validators.maxLength(this.maxLength)])
    })
  }

  onLogin(): void {
    this.isLoading = true
    this.authSvc.getSigned()
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (signedMsg) => {
          let password = this.loginForm.get('password')?.value
          this.walletAddress = this.storageSvc.getAddress()
          let username = this.walletAddress

          this.authSvc.login(username, password, signedMsg, this.authSvc.nonce)
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: (resp) => {
                this.storageService.saveUser(resp);

                this.isLoginFailed = false
                this.isLoggedIn = true
                this.isLoading = false

                this.msgSvc.successfulWalletConnection(this.walletAddress)
                this.msgSvc.successfulLogin()
                this.onLoginEvent.next("login")

                console.log("navigate back to project admin")
                this.router.navigate(['project-admin']).then(() => window.location.reload())

              },
              error: (err) => {
                this.isLoading = false
                this.isLoginFailed = true;
                this.loginForm.get('password')?.markAsPristine()
                this.msgSvc.failedToLogin(err.errorMessage)
              },
            })
        },
        error: (err) => {
          this.isLoading = false
          this.msgSvc.failedToSignMsg("There was an error when logging in")
          this.isSignedMsgFailed = true
        }
      })
  }

  reset() {
    this.isLoginFailed = false
  }

  ngOnDestroy(): void {
    if (this.onWalletAddressSub$) this.onWalletAddressSub$.unsubscribe();
    if (this.onChainIdChangeSub$) this.onChainIdChangeSub$.unsubscribe();
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}