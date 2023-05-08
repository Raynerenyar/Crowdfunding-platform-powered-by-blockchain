import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { liveQuery } from 'dexie';
import { Subject, takeUntil } from 'rxjs';
import { RequestDetails } from 'src/app/model/model';
import { BlockchainService } from 'src/app/services/blockchain.service';
import { DexieDBService } from 'src/app/services/dexie-db.service';

@Component({
  selector: 'app-request-details',
  templateUrl: './request-details.component.html',
  styleUrls: ['./request-details.component.css']
})
export class RequestDetailsComponent implements OnInit, OnDestroy {

  request!: RequestDetails | undefined
  notifier$ = new Subject<boolean>()

  constructor(private bcSvc: BlockchainService, private dexie: DexieDBService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe({
        next: (param: ParamMap) => {
          let requestId = Number(param.get('requestId'))
          console.log(requestId)
          this.dexie.requests.get(requestId).then((request) => this.request = request)
        },
        error: (error: HttpErrorResponse) => console.log(error.message)
        // this.dexie.requests.where('key').equals(requestId!).toArray().then((requests) => {
        //   this.request = requests[0]
        //   console.log(this.request)
        // })
      })
  }

  ngOnDestroy(): void {
    this.notifier$.next(true)
    this.notifier$.unsubscribe()
  }

}
