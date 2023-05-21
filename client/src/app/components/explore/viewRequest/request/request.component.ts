import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { Request } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { SqlRepositoryService } from 'src/app/services/sql.repo.service';

@Component({
  selector: 'app-request',
  templateUrl: './request.component.html',
  styleUrls: ['./request.component.css']
})
export class RequestComponent {

  request!: Request | undefined
  notifier$ = new Subject<boolean>()
  projectAddress!: string
  valueOfVotes!: number
  countOfVotes!: number

  constructor(private bcSvc: BlockchainService, private route: ActivatedRoute, private sqlRepoSvc: SqlRepositoryService, private router: Router) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (param: ParamMap) => {
          let requestId = Number(param.get('requestId'))
          this.projectAddress = param.get('projectAddress')!
          console.log(requestId)
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
              }
            })


        },
        error: (error: HttpErrorResponse) => console.log(error.message)
      })
  }

  goBack() {
    this.router.navigate(['explore', this.projectAddress], { replaceUrl: false });
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }
}
