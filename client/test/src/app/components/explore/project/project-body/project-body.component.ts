import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, takeUntil, timer } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { AuthService } from 'src/app/services/auth.service';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

@Component({
  selector: 'app-project-body',
  templateUrl: './project-body.component.html',
  styleUrls: ['./project-body.component.css']
})
export class ProjectBodyComponent implements OnInit, OnDestroy {

  @Input()
  projectAddress!: string
  tokenAddress!: string
  walletAddress!: string
  tokenBalance!: number
  userAddress!: string

  contributeForm!: FormGroup

  notifier$ = new Subject<boolean>()
  @Input()
  project!: Project
  @Input()
  requests!: Request[]
  timerSub$: any;

  constructor(private repoSvc: SqlRepositoryService, private fb: FormBuilder, private bcSvc: BlockchainService, private msgSvc: PrimeMessageService, private storageSvc: SessionStorageService, private authSvc: AuthService) { }

  ngOnInit(): void {
    this.repoSvc.projectDetailsEvent
      .pipe(takeUntil(this.notifier$))
      .subscribe((project: Project) => {
        console.log(project.tokenName)
        this.project = project
      })
    this.contributeForm = this.fb.group({
      contributeAmount: this.fb.control(null, [Validators.required])
    })
    this.userAddress = this.storageSvc.getAddress()
    this.tokenAddress = this.project.acceptingToken
    this.bcSvc.getBalanceOf(this.userAddress, this.tokenAddress)
      .then(balance => {
        this.tokenBalance = balance
      })
      .catch()
    this.bcSvc.getTokenSymbol(this.tokenAddress)
      .then(symbol => console.log(symbol))
      .catch()
  }

  contribute() {
    let amount = this.contributeForm.get('contributeAmount')?.value

    // approve token before contributing
    this.bcSvc.approveTokenSpendByContract(this.projectAddress, this.tokenAddress, this.walletAddress, amount)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: () => {
          this.msgSvc.generalSuccessMethod("You have successfully approved")
          // on successful approval, send contribute transaction
          this.bcSvc.contribute(this.projectAddress, amount)
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: () => {
                this.msgSvc.generalSuccessMethod("You have successfully contributed.")
                this.authSvc.reloadPage();
              },
              error: () => {
                this.msgSvc.detailedErrorMethod("Execution reverted", "Minimum contribution is 100.")
              }
            })
        },
        error: () => this.msgSvc.generalErrorMethod("Cannot approve token for deduction")
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
