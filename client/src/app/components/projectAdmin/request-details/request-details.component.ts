import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Project, Request } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { PrimeMessageService } from 'src/app/services/prime.message.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

@Component({
  selector: 'app-request-details',
  templateUrl: './request-details.component.html',
  styleUrls: ['./request-details.component.css']
})
export class RequestDetailsComponent implements OnInit, OnDestroy {

  request!: Request | undefined
  notifier$ = new Subject<boolean>()
  projectAddress!: string
  valueOfVotes!: number
  countOfVotes!: number
  project!: Project

  constructor(
    private bcSvc: BlockchainService,
    private route: ActivatedRoute,
    private sqlRepoSvc: SqlRepositoryService,
    private router: Router,
    private msgSvc: PrimeMessageService
  ) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        let requestId = Number(param.get('requestId'))
        this.projectAddress = param.get('address')!
        if (!this.sqlRepoSvc.project) {
          this.router.navigate(['project-admin', this.projectAddress])
        } else {
          this.sqlRepoSvc.getSingleRequest(requestId)
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

                this.project = this.sqlRepoSvc.project
              }
            })
        }
      })
  }

  goBack() {
    this.router.navigate(['project-admin', this.projectAddress], { replaceUrl: false });
  }

  collectContributions() {
    this.bcSvc.receiveContribution(this.projectAddress, this.request!.requestNo)
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: () => this.msgSvc.generalSuccessMethod(`You have collected the contributions of request ${this.request!.requestNo}`),
        error: (err) => this.msgSvc.generalErrorMethod('Unable to collect the contributions')
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
