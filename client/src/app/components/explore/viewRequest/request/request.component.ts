import { AfterViewInit, Component, ElementRef, Input, OnDestroy, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';
import { WalletService } from 'src/app/services/wallet.service';

@Component({
  selector: 'app-request',
  templateUrl: './request.component.html',
  styleUrls: ['./request.component.css']
})
export class RequestComponent implements OnDestroy {

  request!: Request
  projectAddress!: string
  tokenAddress!: string
  project!: Project

  walletAddress!: string
  notifier$ = new Subject<true>()
  valueOfVotes!: number
  countOfVotes!: number
  notRefundable = true
  requestId!: number
  isLoading = false

  deadlineTimestamp!: number

  @ViewChild('contributeReq')
  contributeBody!: ElementRef
  @Output()
  contributeHeight = new Subject<number>()


  constructor(
    private fb: FormBuilder,
    private bcSvc: BlockchainService,
    private storageSvc: SessionStorageService,
    private msgSvc: PrimeMessageService,
    private router: Router,
    private route: ActivatedRoute,
    private sqlRepoSvc: SqlRepositoryService,
    private walletSvc: WalletService) { }

  ngOnInit(): void {
    this.walletAddress = this.storageSvc.getAddress()

    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        this.projectAddress = param.get('projectAddress')!
        if (!this.sqlRepoSvc.project) {
          this.router.navigate(['/explore', this.projectAddress])
        } else {
          this.project = this.sqlRepoSvc.project
          this.projectAddress = this.project.projectAddress

          this.bcSvc.getBlockTimestamp()
            .pipe(takeUntil(this.notifier$))
            .subscribe((blockTimestamp) => {
              let bts = blockTimestamp as number
              let date = new Date(this.project.deadline)
              this.deadlineTimestamp = date.getTime()
              console.log(bts, this.deadlineTimestamp)
              if (bts >= this.deadlineTimestamp) {
                this.notRefundable = false
              }
            })

          this.requestId = parseInt(param.get('requestId')!)
          // get request from db
          this.sqlRepoSvc.getSingleRequest(this.requestId)
            .pipe(takeUntil(this.notifier$))
            .subscribe({
              next: (request: Request) => {
                this.request = request

                this.sqlRepoSvc.getValueOfVotes(this.projectAddress, request.requestNo)
                  .pipe(takeUntil(this.notifier$))
                  .subscribe({
                    next: (value: number) => {
                      this.valueOfVotes = value
                    }
                  })

                this.sqlRepoSvc.getCountOfVotes(this.projectAddress, request.requestNo)
                  .pipe(takeUntil(this.notifier$))
                  .subscribe({
                    next: (value: number) => {
                      this.countOfVotes = value
                    }
                  })
              },
              error: (err) => {
                this.router.navigate(['explore', this.projectAddress])
              },
            })
        }
      })
  }

  voteRequest() {
    if (this.walletSvc.isOnRightChain()) {
      this.isLoading = true
      this.bcSvc.voteRequest(this.projectAddress, this.request.requestNo)
        .pipe(takeUntil(this.notifier$))
        .subscribe({
          next: () => {
            this.isLoading = false
            this.msgSvc.generalSuccessMethod("You have successfully voted")
            window.location.reload()
          },
          error: (error) => {
            this.isLoading = false
            this.msgSvc.generalErrorMethod("There was an error, failed to vote")
          }

        })
    } else this.msgSvc.tellToConnectToChain()
  }

  goBack() {
    console.log("bo gakc")
    this.router.navigate(['explore', this.projectAddress])
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
