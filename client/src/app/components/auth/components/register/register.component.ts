/* component binds form data (username, email, password) 
from template to AuthService.register() method that returns 
an Observable object. */
import { ChangeDetectorRef, Component, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject, Subscription, takeUntil } from 'rxjs';
import { WalletService } from 'src/app/services/wallet.service';
import { AuthService as AuthzService } from '../../../../services/auth.service'
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SessionStorageService } from '../../../../services/session.storage.service';
import { Router } from '@angular/router';
import { OverlayPanelComponent } from 'src/app/layout/overlayPanel/overlay.panel.component';
import { HeaderComponent } from 'src/app/layout/header/header.component';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {
  form: any = {
    username: null,
    email: null,
    password: null
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  roles: string[] = [];

  isLoggedIn = false;
  isWalletSigned = false;
  isSignedMsgFailed = false;

  @Output()
  onRegisterEvent = new Subject()

  registerForm!: FormGroup
  walletAddress!: string
  chainId!: number
  chainName!: string
  shortenedAddress!: string
  minLength = 10
  maxLength = 20


  onWalletAddressSub$!: Subscription
  onChainIdChangeSub$!: Subscription
  onSigningInWeb3Sub$!: Subscription
  onRegisteringSub$!: Subscription
  notifier$ = new Subject<boolean>()
  onRegistering = false

  constructor(private storageService: SessionStorageService, private walletSvc: WalletService, private cdr: ChangeDetectorRef, private fb: FormBuilder, private authSvc: AuthzService, private msgSvc: PrimeMessageService, private storageSvc: SessionStorageService, private router: Router) { }

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      const user = this.storageSvc.getUser();
      this.roles = user.roles;
      this.isLoggedIn = this.roles.includes('ROLE_USER')
    }
    this.onWalletAddressSub$ = this.walletSvc.walletAddressEvent.subscribe((address) => {
      this.walletAddress = address
      this.shortenedAddress = this.walletSvc.shortenAddress(address)
      this.cdr.detectChanges()
    })
    this.onChainIdChangeSub$ = this.walletSvc.onChainIdChangeEvent.subscribe((chainId) => {
      this.chainId = chainId
      if (chainId == 11155111) this.chainName = "Sepolia"
      if (chainId == 1337) this.chainName = "Ganache"
      this.cdr.detectChanges()
    })
    this.registerForm = this.fb.group({
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(this.minLength), Validators.maxLength(this.maxLength)])
    })
  }

  onRegister(): void {
    this.onRegistering = true
    this.authSvc.getSigned()
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (signedMsg) => {
          let password = this.registerForm.get('password')?.value
          let username = this.walletAddress
          console.log("registering")

          this.authSvc.register(username, password, signedMsg, this.authSvc.nonce).subscribe({
            next: data => {
              // TODO: reload and ask to sign in
              console.log(data);
              this.isSuccessful = true;
              this.isSignUpFailed = false;
              this.onRegisterEvent.next(true)
              this.router.navigate(['login']).then(() => window.location.reload())

            },
            error: err => {
              this.errorMessage = err.error.message;
              this.msgSvc.failedToRegister(err.error.message)
              this.isSignUpFailed = true;
              this.onRegistering = false
              this.router.navigate(['login']).then(() => window.location.reload())
            }
          })
        },
        error: (err) => {
          console.log(err)
          this.onRegistering = false
          this.msgSvc.failedToSignMsg(err)
          this.isSignedMsgFailed = true;
        },
      })

  }

  ngOnDestroy(): void {
    if (this.onWalletAddressSub$) this.onWalletAddressSub$.unsubscribe()
    if (this.onChainIdChangeSub$) this.onChainIdChangeSub$.unsubscribe()
    if (this.onSigningInWeb3Sub$) this.onSigningInWeb3Sub$.unsubscribe()
    if (this.onRegisteringSub$) this.onRegisteringSub$.unsubscribe()
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}