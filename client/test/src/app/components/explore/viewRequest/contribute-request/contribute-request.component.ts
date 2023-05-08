import { AfterViewInit, Component, ElementRef, Input, OnDestroy, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { ProjectDetails, RequestDetails } from 'src/app/model/model';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SessionStorageService } from 'src/app/services/session.storage.service';

@Component({
  selector: 'app-contribute-request',
  templateUrl: './contribute-request.component.html',
  styleUrls: ['./contribute-request.component.css']
})
export class ContributeRequestComponent implements OnDestroy, AfterViewInit {
  currentRequest!: FormGroup

  @Input()
  request!: RequestDetails
  @Input()
  tokenBalance!: number
  @Input()
  projectAddress!: string
  @Input()
  tokenAddress!: string
  @Input()
  project!: ProjectDetails

  walletAddress!: string
  notifier$ = new Subject<true>()

  notRefundable = true

  @ViewChild('contributeReq')
  contributeBody!: ElementRef
  @Output()
  contributeHeight = new Subject<number>()


  constructor(private fb: FormBuilder, private bcSvc: BlockchainService, private storageSvc: SessionStorageService, private msgSvc: PrimeMessageService) { }

  ngOnInit(): void {
    this.currentRequest = this.fb.group({
      contributeAmount: this.fb.control(null, [Validators.required]),
      voteRequest: this.fb.control<number>(0, [Validators.required]),
    })
    this.walletAddress = this.storageSvc.getAddress()
    this.bcSvc.getBlockTimestamp()
      .pipe(takeUntil(this.notifier$))
      .subscribe((blockTimestamp) => {
        let bts = blockTimestamp as number
        let date = new Date(this.project.deadline)
        let deadlineTimestamp = date.getTime()
        if (bts >= deadlineTimestamp) {
          this.notRefundable = false
        }
      })
    console.log(this.tokenBalance)
    console.log(this.projectAddress)
  }

  ngAfterViewInit(): void {
    // contributeReq
    console.log(this.contributeBody.nativeElement.offsetHeight)
    this.contributeHeight.next(this.contributeBody.nativeElement.offsetHeight)

  }

  voteRequest() {
    let requestNum: number = this.currentRequest.get('voteRequest')?.value
    console.log(this.request.requestId)
    this.bcSvc.voteRequest(this.projectAddress, this.request.requestId)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: () => this.msgSvc.generalSuccessMethod("You have successfully voted"),
        error: () => this.msgSvc.generalErrorMethod("There was an error, failed to vote")
      })
    // this.bcSvc.voteRequest(requestNum)
  }

  contribute() {
    let amount = this.currentRequest.get('contributeAmount')?.value

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
              next: () => this.msgSvc.generalSuccessMethod("You have successfully contributed."),
              error: () => this.msgSvc.generalErrorMethod("There was an error, failed to contribute.")
            })
        },
        error: () => this.msgSvc.generalErrorMethod("cannot approve token for deduction")
      })
  }

  refund() {
    this.bcSvc.refund(this.projectAddress)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: () => this.msgSvc.generalSuccessMethod("You have successfully gotten your refund"),
        error: () => this.msgSvc.generalErrorMethod("Failed to get refund")
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
