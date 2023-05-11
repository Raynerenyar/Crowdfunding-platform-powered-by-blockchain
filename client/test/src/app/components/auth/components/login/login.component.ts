/* Login Component also uses AuthService to work with
 Observable object. Besides that, it calls StorageService 
 methods to check loggedIn status and save User info to Session Storage. */
import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { SessionStorageService } from '../../../../services/session.storage.service';
import { WalletService } from 'src/app/services/wallet.service';
import { Subject, Subscription, timer } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService as AuthzService } from '../../../../services/auth.service'
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
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  loginForm!: FormGroup
  walletAddress!: string
  chainId!: number
  chainName!: string
  shortenedAddress!: string
  minLength = 10
  maxLength = 20
  isWalletSigned = false;
  isSignedMsgFailed = false;

  onChainIdChangeSub$!: Subscription
  onWalletAddressSub$!: Subscription
  onLoginSub$!: Subscription
  onSigningInWeb3$!: Subscription
  timerSub$!: Subscription

  @Output()
  onLoginEvent = new Subject<string>()

  constructor(private storageService: SessionStorageService, private walletSvc: WalletService, private cdr: ChangeDetectorRef, private fb: FormBuilder, private authSvc: AuthzService, private msgSvc: PrimeMessageService, private router: Router, private storageSvc: SessionStorageService) { }

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
      this.roles = this.storageService.getUser().roles;
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
    this.loginForm = this.fb.group({
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(this.minLength), Validators.maxLength(this.maxLength)])
    })
  }

  onLogin(): void {
    // const { username, password } = this.form;

    // this.authService.login(username, password).subscribe({
    //   next: data => {
    //     this.storageService.saveUser(data);

    //     this.isLoginFailed = false;
    //     this.isLoggedIn = true;
    //     this.roles = this.storageService.getUser().roles;
    //     this.reloadPage();
    //   },
    //   error: err => {
    //     this.errorMessage = err.error.message;
    //     this.isLoginFailed = true;
    //   }
    // });

    this.onSigningInWeb3$ = this.authSvc.signInWithWeb3()
      .subscribe({
        next: token => {
          console.log(token)
          this.isWalletSigned = true;
          this.isSignedMsgFailed = false
          this.msgSvc.successfulWalletConnection(this.walletAddress)

          let pazzword = this.loginForm.get('password')?.value
          let uzername = this.walletAddress
          this.onLoginSub$ = this.authSvc.login(uzername, pazzword)
            .subscribe({
              next: data => {
                this.storageService.saveUser(data);

                this.isLoginFailed = false;
                this.isLoggedIn = true;
                this.roles = this.storageService.getUser().roles;
                this.storageSvc.saveAddress(this.walletAddress)
                this.msgSvc.successfulLogin()
                this.onLoginEvent.next("login")

                // TODO: 
                // do router instead of reload
                // this.router.navigate()
                this.router.navigate(['project-admin'])
                this.reloadPage() // comment out for now, later enable it again

              },
              error: err => {
                console.log(err.errorMessage)
                this.isLoginFailed = true;
                this.msgSvc.failedToLogin(err.errorMessage)
              }
            })
        },
        error: error => {
          console.log(error)
          this.msgSvc.failedToSignMsg(error)
          this.isSignedMsgFailed
        }
      })
  }

  reloadPage(): void {
    this.timerSub$ = timer(3000).subscribe(t => {
      window.location.reload();
    })
  }

  ngOnDestroy(): void {
    if (this.onWalletAddressSub$) this.onWalletAddressSub$.unsubscribe();
    if (this.onChainIdChangeSub$) this.onChainIdChangeSub$.unsubscribe();
    if (this.onLoginSub$) this.onLoginSub$.unsubscribe();
    if (this.onSigningInWeb3$) this.onSigningInWeb3$.unsubscribe();
  }

}