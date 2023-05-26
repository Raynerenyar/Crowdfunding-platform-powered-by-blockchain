import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Subject, takeUntil } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { AuthService } from 'src/app/services/auth.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-project-main',
  templateUrl: './project-main.component.html',
  styleUrls: ['./project-main.component.css']
})
export class ProjectMainComponent implements OnInit, OnDestroy {

  contributeForm!: FormGroup
  projectAddress!: string
  project!: Project
  requests!: Request[]
  userAddress!: string
  tokenBalance = 0
  contributedAmount = 0
  currBlockTimestamp!: number
  deadlineTimestamp!: number

  showContribute = true
  showRefund = false
  notRefundable = true
  raisedAmountCondition = false
  contributedCondition = false
  blockTimestampCondition = false
  isLoading = false
  notifier$ = new Subject<boolean>()

  constructor(
    private route: ActivatedRoute,
    private repoSvc: SqlRepositoryService,
    private router: Router,
    private bcSvc: BlockchainService,
    private storageSvc: SessionStorageService,
    private fb: FormBuilder,
    private msgSvc: PrimeMessageService,
    private authSvc: AuthService,
    private walletSvc: WalletService,
  ) { }

  ngOnInit(): void {
    this.userAddress = this.storageSvc.getAddress()
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((params: ParamMap) => {
        this.projectAddress = params.get('projectAddress')!

        // get project
        this.repoSvc.getSingleProject(this.projectAddress)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: async (project) => {
              this.project = project as Project

              /* assign to repo service project property so that
               * request page can retrieve it */
              this.repoSvc.project = project

              this.contributeForm = this.fb.group({
                amount: this.fb.control(null, [Validators.required])
              })

              // get requests
              this.repoSvc.getRequests(this.projectAddress)
                .pipe(takeUntil(this.notifier$))
                .subscribe({
                  next: (requests: Request[]) => {
                    this.requests = requests
                    // console.log(requests)
                    this.project.numOfRequests = (requests) ? requests.length : 0
                  },
                  error: (error: HttpErrorResponse) => {
                    console.log("not found")
                  }
                })

              // get balance of wallet that is connected
              if (this.userAddress) {
                this.bcSvc.getBalanceOf(this.userAddress, this.project.acceptingToken)
                  .then(balance => {
                    this.tokenBalance = balance
                  })
                  .catch()
              }

              this.raisedAmountCondition = await this.getRaisedAmount()
              this.contributedCondition = await this.getContributedAmount()
              this.blockTimestampCondition = await this.getBlockTimestamp()

              // same condition as in smart contract
              // if all true then enable the refund button
              let conditionsForRefund = [
                this.raisedAmountCondition,
                this.contributedCondition,
                this.blockTimestampCondition
              ]

              const result = await Promise.all(conditionsForRefund)
              // console.log(result)
              if (!result.includes(false))
                this.notRefundable = false
            },
            error: (error: HttpErrorResponse) => {
              console.log(error.status)
            }
          })
      })
  }

  // returns timestamp condition for refund
  getBlockTimestamp(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.bcSvc.getBlockTimestamp()
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (blockTimestamp: number) => {
            let date = new Date(this.project.deadline)
            this.deadlineTimestamp = date.getTime()
            this.currBlockTimestamp = blockTimestamp
            resolve(this.currBlockTimestamp >= this.deadlineTimestamp)
          },
          error: () => reject(false)
        })
    })
  }

  // returns contributed amount condition for refund
  getContributedAmount(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.bcSvc.getContributionAmount(this.projectAddress, this.userAddress, this.project.acceptingToken)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (value) => {
            this.contributedAmount = value
            // console.log("contributed amount", this.contributedAmount)
            resolve(this.contributedAmount > 0)
          },
          error: (err) => reject(false)
        })
    })
  }

  // returns raised amount condition for refund
  getRaisedAmount(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      this.bcSvc.getRaisedAmount(this.project.projectAddress, this.project.acceptingToken)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: (value) => {
            this.project.raisedAmount = value
            // console.log("raised amount", this.project.raisedAmount)
            resolve(this.project.raisedAmount >= this.project.goal)
          },
          error: (err) => reject(false)
        })
    })
  }




  contributeTab() {
    this.showContribute = true
    this.showRefund = false
  }

  refundTab() {
    this.showContribute = false
    this.showRefund = true
  }

  goBack() {
    this.router.navigate(['explore'])
  }

  goAnnouncement() {
    this.router.navigate(['explore', this.projectAddress, 'announcements'])
  }

  goComment() {
    this.router.navigate(['explore', this.projectAddress, 'comments'])
  }

  getRequest(requestId: number) {
    this.router.navigate(['explore', this.projectAddress, 'request', requestId])
  }

  onContribute() {

    if (this.walletSvc.isOnRightChain()) {

      let walletAddress = this.storageSvc.getAddress()
      let contributeAmount = this.contributeForm.get('amount')?.value
      if (this.contributeForm.valid) {
        this.isLoading = true
        // approve token before contributing
        this.bcSvc.approveToken(this.project.acceptingToken, this.projectAddress, walletAddress, contributeAmount).then(
          () => {
            this.msgSvc.generalSuccessMethod("You have successfully approved")

            // on successful approval, send contribute transaction
            this.bcSvc.contribute(this.projectAddress, contributeAmount)
              .pipe(takeUntil(this.notifier$))
              .subscribe({
                next: () => {
                  this.isLoading = false
                  this.msgSvc.generalSuccessMethod("You have successfully contributed.")
                  this.authSvc.reloadPage();
                },
                error: () => {
                  this.isLoading = false
                  this.msgSvc.detailedErrorMethod("Execution reverted", "Minimum contribution is 100.")
                }
              })
          })
        return
      }
      this.msgSvc.generalInfoMethod("You have to enter an amount to contribute")

    } else this.msgSvc.tellToConnectToChain()

  }

  onRefund() {
    if (this.walletSvc.isOnRightChain()) {
      console.log("not refundable?", this.notRefundable)
      let refundable = !this.notRefundable
      if (refundable) {
        this.isLoading = true
        this.bcSvc.getRefund(this.projectAddress)
          .pipe(takeUntil(this.notifier$))
          .subscribe({
            next: () => {
              this.isLoading = false
              this.msgSvc.generalSuccessMethod("You have successfully gotten your refund")
              window.location.reload()
            },
            error: () => {
              this.isLoading = false
              this.msgSvc.generalErrorMethod("Failed to get refund")
            }
          })
      }
    } else this.msgSvc.tellToConnectToChain()
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
    this.bcSvc.cleanUp()
    this.authSvc.cleanUp()
  }
}
