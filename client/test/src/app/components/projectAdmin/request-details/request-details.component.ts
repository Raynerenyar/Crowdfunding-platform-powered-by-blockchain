import { Component, OnInit } from '@angular/core';
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
export class RequestDetailsComponent implements OnInit {

  request!: RequestDetails | undefined
  notifier$ = new Subject<boolean>()

  constructor(private bcSvc: BlockchainService, private dexie: DexieDBService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.paramMap
      .pipe(takeUntil(this.notifier$))
      .subscribe((param: ParamMap) => {
        let requestId = Number(param.get('requestId'))
        this.dexie.requests.get(requestId).then((request) => {
          this.request = request
        })
        // this.dexie.requests.where('key').equals(requestId!).toArray().then((requests) => {
        //   this.request = requests[0]
        //   console.log(this.request)
        // })
      })
  }

}
