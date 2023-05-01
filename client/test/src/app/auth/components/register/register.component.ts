/* component binds form data (username, email, password) 
from template to AuthService.register() method that returns 
an Observable object. */
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { WalletService } from 'src/app/services/wallet.service';
import { AuthService as AuthzService } from '../../../services/auth.service'
import { alertMessageService } from 'src/app/services/alert.message.service';
import { StorageService } from '../../../services/storage.service';

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

  isLoggedIn = false;
  isWalletSigned = false;
  isSignedMsgFailed = false;

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

  constructor(private authService: AuthService, private storageService: StorageService, private walletSvc: WalletService, private cdr: ChangeDetectorRef, private fb: FormBuilder, private authSvc: AuthzService, private msgSvc: alertMessageService) { }

  ngOnInit(): void {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
      // this.roles = this.storageService.getUser().roles;
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
    // const { username, email, password } = this.form;
    // const username = this.walletAddress
    // const email = "ssts@gmail.com"
    // const password = this.registerForm.get('password')?.value
    // this.authService.register(username, email, password).subscribe({
    //   next: data => {
    //     console.log(data);
    //     this.isSuccessful = true;
    //     this.isSignUpFailed = false;
    //   },
    //   error: err => {
    //     this.errorMessage = err.error.message;
    //     this.isSignUpFailed = true;
    //     console.log(this.errorMessage)
    //   }
    // });

    let pazzword = this.registerForm.get('password')?.value
    let uzername = this.walletAddress
    console.log("registering")
    this.authSvc.signInWithWeb3().subscribe({
      next: token => {
        console.log(token)
        this.isWalletSigned = true;
        this.isSignedMsgFailed = false

        this.authSvc.register(uzername, pazzword).subscribe({
          next: data => {
            console.log(data);
            this.isSuccessful = true;
            this.isSignUpFailed = false;
          },
          error: err => {
            this.errorMessage = err.error.message;
            this.msgSvc.failedToRegister(err.error.message)
            this.isSignUpFailed = true;
          }
        })
      },
      error: error => {
        console.log(error)
        this.msgSvc.failedToSignMsg(error)
        this.isSignedMsgFailed = true;
      }
    })
  }

  ngOnDestroy(): void {
    if (this.onWalletAddressSub$) this.onWalletAddressSub$.unsubscribe()
    if (this.onChainIdChangeSub$) this.onChainIdChangeSub$.unsubscribe()
    if (this.onSigningInWeb3Sub$) this.onSigningInWeb3Sub$.unsubscribe()
    if (this.onRegisteringSub$) this.onRegisteringSub$.unsubscribe()
  }
}