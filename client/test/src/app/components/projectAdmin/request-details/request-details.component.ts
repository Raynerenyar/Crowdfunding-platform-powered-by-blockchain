import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { liveQuery } from 'dexie';
import { Subject, takeUntil } from 'rxjs';
import { Request } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { DexieDBService } from 'src/app/services/dexie-db.service';
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

  constructor(private bcSvc: BlockchainService, private dexie: DexieDBService, private route: ActivatedRoute, private sqlRepoSvc: SqlRepositoryService, private router: Router) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (param: ParamMap) => {
          let requestId = Number(param.get('requestId'))
          this.projectAddress = param.get('address')!
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
        // this.dexie.requests.where('key').equals(requestId!).toArray().then((requests) => {
        //   this.request = requests[0]
        //   console.log(this.request)
        // })
      })
  }

  goBack() {
    this.router.navigate(['project-admin', this.projectAddress], { replaceUrl: false });
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
