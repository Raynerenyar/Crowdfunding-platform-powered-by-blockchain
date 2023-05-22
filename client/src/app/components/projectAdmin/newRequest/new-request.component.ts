import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { ValidatorService } from 'src/app/services/validator.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-new-request',
  templateUrl: './new-request.component.html',
  styleUrls: ['./new-request.component.css']
})
export class NewRequestComponent implements OnInit, OnDestroy {

  newRequestForm!: FormGroup
  projectAddress!: string
  charCount = 0
  charLimit = 2000
  notifier$ = new Subject<boolean>()
  submitted = false

  constructor(
    private fb: FormBuilder,
    private bcSvc: BlockchainService,
    private repoSvc: SqlRepositoryService,
    private route: ActivatedRoute,
    private storageSvc: SessionStorageService,
    private router: Router,
    private validatorSvc: ValidatorService,
    private msgSvc: PrimeMessageService,
    private walletSvc: WalletService) { }

  ngOnInit(): void {
    if (this.repoSvc.requests) {
      let walletAddress = this.storageSvc.getUser()
      this.newRequestForm = this.fb.group({
        title: this.fb.control(null, [Validators.required]),
        description: this.fb.control(null, [Validators.required, Validators.maxLength(this.charLimit)]),
        recipient: this.fb.control<string>(walletAddress.username, [Validators.required, this.validatorSvc.addressValidator()]),
        amount: this.fb.control(null, [Validators.required])
      })

      this.route.paramMap.subscribe((params: ParamMap) => {
        let address = params.get('address')
        if (address) this.projectAddress = address
      })
    } else {
      this.router.navigate(['project-admin'])
    }
  }

  createRequest() {
    if (this.walletSvc.isOnRightChain()) {
      this.submitted = true
      if (this.newRequestForm.valid) {

        let title = this.newRequestForm.get('title')?.value
        let description = this.newRequestForm.get('description')?.value
        let recipient = this.newRequestForm.get('recipient')?.value
        let amount = this.newRequestForm.get('amount')?.value
        if (this.calculateAmountLeftover() > amount) {
          this.bcSvc.createRequest(this.projectAddress, title, description, recipient, amount)
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: () => this.router.navigate(['project-admin', this.projectAddress]),
              error: () => { }
            })
        } else {
          this.msgSvc.generalErrorMethod("This request exceeds the raised amount")
        }
      }
    } else this.msgSvc.tellToConnectToChain()
  }

  calculateAmountLeftover(): number {
    let totalReqAmount = 0
    this.repoSvc.requests.forEach(request => {
      totalReqAmount += request.amount
    });
    return this.repoSvc.project.raisedAmount - totalReqAmount
  }

  goBack() {
    console.log("navigating back to project admin")
    this.router.navigate(['project-admin', this.projectAddress])
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
