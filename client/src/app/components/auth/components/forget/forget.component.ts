import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-forget',
  templateUrl: './forget.component.html',
  styleUrls: ['./forget.component.css']
})
export class ForgetComponent implements OnInit {
  newPasswordForm!: FormGroup
  isLoggedIn = false
  isLoginFailed = false
  isLoading = false
  walletAddress!: string
  shortenedAddress!: string
  chainName = "Unknown"

  minLength = 10
  maxLength = 20
  notifier$ = new Subject<boolean>()

  constructor(
    private fb: FormBuilder,
    private walletSvc: WalletService,
    private storageSvc: SessionStorageService,
    private authSvc: AuthService,
    private msgSvc: PrimeMessageService,
    private router: Router
  ) { }
  ngOnInit(): void {
    this.newPasswordForm = this.fb.group({
      password: this.fb.control(null, [Validators.required, Validators.minLength(this.minLength), Validators.maxLength(this.maxLength)])
    })
    this.walletAddress = this.storageSvc.getAddress()
    this.shortenedAddress = this.walletSvc.shortenAddress(this.walletAddress)
    this.chainName = this.storageSvc.getChain()
  }
  submit() {
    if (this.newPasswordForm.valid) {
      this.isLoading = true
      this.authSvc.getSigned()
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (signedMsg) => {
            let password = this.newPasswordForm.get('password')?.value
            this.walletAddress = this.storageSvc.getAddress()
            let username = this.walletAddress

            this.authSvc.changePassword(username, password, signedMsg, this.authSvc.nonce)
              .pipe(takeUntil(this.notifier$))
              .subscribe({
                next: (resp) => {

                  this.isLoginFailed = false
                  this.isLoggedIn = true
                  this.isLoading = false

                  console.log("navigate to login")
                  this.router.navigate(['login']).then(() => window.location.reload())

                },
                error: (err) => {
                  this.isLoading = false
                  this.isLoginFailed = true;
                  this.newPasswordForm.get('password')?.markAsPristine()
                  this.msgSvc.failedToLogin(err.errorMessage)
                },
              })
          },
          error: (err) => {
            this.isLoading = false
            this.msgSvc.failedToSignMsg("There was an error when logging in")
          }
        })
    }
  }
}
